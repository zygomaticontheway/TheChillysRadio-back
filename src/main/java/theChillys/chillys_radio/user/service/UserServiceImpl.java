package theChillys.chillys_radio.user.service;

//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
//import org.springframework.cglib.core.Local;
//import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestClientResponseException;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.exception.UserNotFoundException;
//import theChillys.chillys_radio.mail.ChillysRadioMailSender;
import theChillys.chillys_radio.mail.MailTemplatesUtil;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.ConfirmationCode;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IConfirmationCodesRepository;
import theChillys.chillys_radio.user.repository.IUserRepository;

//import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final IUserRepository repository;
    private final IRoleService roleService;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final IStationRepository stationRepository;
//  private final UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration;
    private final IConfirmationCodesRepository confirmationCodeRepository;
    private final MailTemplatesUtil mailTemplatesUtil;



    public User findUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id:" + userId + " not found"));
    }

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> customers = repository.findAll();

        return customers.stream().map(c -> mapper.map(c, UserResponseDto.class)).toList();
    }

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        checkUserData(dto);

//        checkIfUserExistsByName(dto);
        checkIfUserExistsByEmail(dto);

        Role role = roleService.getRoleByTitle("ROLE_USER");

        String encodedPass = encoder.encode(dto.getPassword());

        User newUser = createNewUserFromDto(dto, encodedPass, role);

        User savedUser = repository.save(newUser);

        String codeValue = UUID.randomUUID().toString();

        saveConfirmCode(codeValue, newUser);

        String link = createConfirmationLink(codeValue);

        String html = mailTemplatesUtil.createEmailTemplate(newUser, link);

        mailTemplatesUtil.sendMail(newUser, html);

        return mapper.map(savedUser, UserResponseDto.class);
    }

    private static User createNewUserFromDto(UserRequestDto dto, String encodedPass, Role role) {
        User newUser = new User();
        newUser.setName(dto.getName().isEmpty() ? "No name" : dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(encodedPass);
        newUser.setRoles(Collections.singleton(role));
        newUser.setFavorites(Collections.emptySet());
        newUser.setState(User.State.NOT_CONFIRMED);
        return newUser;
    }


    private static String createConfirmationLink(String codeValue) {
        String link = "<a href='https://chillysradio.site/#/confirm?id=" + codeValue + "'>Confirm Registration</a>";
        return link;
    }

    private void saveConfirmCode(String codeValue, User newUser) {
        ConfirmationCode code = new ConfirmationCode();
        code.setCode(codeValue);
        code.setUser(newUser);
        code.setExpiredDateTime(LocalDateTime.now().plusWeeks(1));

        confirmationCodeRepository.save((code));
    }

    private void checkIfUserExistsByName(UserRequestDto dto) {
        repository.findUserByName(dto.getName()).ifPresent(u -> {
            throw new RuntimeException("User " + dto.getName() + " already exists");
        });
    }

    private void checkIfUserExistsByEmail(UserRequestDto dto) {
        repository.findUserByEmail(dto.getEmail()).ifPresent(u -> {
            throw new RuntimeException("User " + dto.getName() + " with Email " + dto.getEmail() + " already exists");
        });
    }

    private static void checkUserData(UserRequestDto dto) {
//        if (dto.getName() == null || dto.getName().isEmpty()) {
//            throw new IllegalArgumentException("User name is required");
//        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    @Transactional
    public UserResponseDto confirm (String confirmCode){

        ConfirmationCode code = confirmationCodeRepository
                .findByCodeAndExpiredDateTimeAfter(confirmCode, LocalDateTime.now())
                .orElseThrow(RuntimeException::new);

        User user = repository
                .findFirstByCodesContains(code)
                .orElseThrow(() -> new UserNotFoundException("User with confirmation code " + confirmCode + " not found") );

        user.setState(User.State.CONFIRMED);

        return mapper.map(user, UserResponseDto.class);
    }


    @Override
    @Transactional
    public UserResponseDto setAdminRole(String email) {

        User user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        if (!user.getRoles().contains(roleService.getRoleByTitle("ROLE_ADMIN"))) {
            Set<Role> roles = user.getRoles();
            roles.add(roleService.getRoleByTitle("ROLE_ADMIN"));
            user.setRoles(roles);
            repository.save(user);
        } else {
            throw new RuntimeException("User is already admin");
        }

        return mapper.map(user, UserResponseDto.class);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserRequestDto dto) {
        User user = findUserById(userId);
//        user.setId(userId);// wtf? What for?

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        user.setRoles(user.getRoles());
        user.setFavorites(user.getFavorites());

        User savedUser = repository.save(user);

        return mapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    @Transactional
    public UserResponseDto changePassword(String email, String oldPassword, String newPassword) {
        User user = repository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email: " + email + " not found"));

        if(!encoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password incorrect");
        }

        String encodedPass = encoder.encode(newPassword);
        user.setPassword(encodedPass);
        User savedUser = repository.save(user);

        return mapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {

        return Optional.ofNullable(mapper.map(findUserById(id), UserResponseDto.class));
    }

    @Override
    public List<UserResponseDto> findUsersByNameOrEmail(String name, String email) {
        List<User> users = repository.findByNameContainingOrEmailContaining(name, email);

        if(users.isEmpty()) {
            throw new UserNotFoundException(("User with name: " + name + " or " + email + "not found"));
        } else {
            return users.stream()
                    .map(user -> mapper.map(user, UserResponseDto.class)).toList();
        }
    }

    public UserResponseDto getUserResponseDtoByName(String name) {

        User user = repository.findUserByName(name).orElseThrow(() -> new UserNotFoundException("User with name: " + name + " not found"));

        return mapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto getUserResponseDtoByEmail(String email) {

        User user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        return mapper.map(user, UserResponseDto.class);
    }

    //как spring получает User по логину - логин - это name!
    @Override
    public UserDetails loadUserByUsername(String name) throws UserNotFoundException {

        return repository.findUserByName(name)
                .orElseThrow(() -> new UserNotFoundException("User with name: " + name + " not found"));
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws UserNotFoundException {
        return repository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Override
    public List<StationResponseDto> getUsersFavoriteStations(String email) {

        UserResponseDto user = getUserResponseDtoByEmail(email);

        return user.getFavorites();
    }

    @Override
    public List<StationResponseDto> toggleFavoriteStation(String email, String stationuuid) {

        User user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));
        Station station = stationRepository.findByStationuuid(stationuuid).orElseThrow(() -> new StationNotFoundException("Station with stationuuid: " + stationuuid + " not exist"));

        if (user.getFavorites().contains(station)) {
            user.getFavorites().remove(station);
        } else {
            user.getFavorites().add(station);
        }
        repository.save(user);
        return getUsersFavoriteStations(email);
    }
}
