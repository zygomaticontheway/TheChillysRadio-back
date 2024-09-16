package theChillys.chillys_radio.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.exception.UserNotFoundException;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationRequestDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IUserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

    private final IUserRepository repository;
    private final IRoleService roleService;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final IStationRepository stationRepository;
//  private final UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration;

    public User findUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id:" + userId + " not found"));
    }

    @Override
    public UserResponseDto getUsersFavoriteStations(Long userId) {

        User user = findUserById(userId);

        List<StationResponseDto> favoriteStationsDto = user.getFavorites().stream()
                .map(st -> mapper.map(st, StationResponseDto.class))
                .collect(Collectors.toList());

        Set<Role> roles = user.getRoles();

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                favoriteStationsDto,
                roles);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> customers = repository.findAll();

        return customers.stream().map(c -> mapper.map(c, UserResponseDto.class)).toList();
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        repository.findUserByName(dto.getName()).ifPresent(u -> {
            throw new RuntimeException("User " + dto.getName() + " already exists");
        });

        Role role = roleService.getRoleByTitle("ROLE_USER");

        String encodedPass = encoder.encode(dto.getPassword());

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(encodedPass);
        newUser.setRoles(Collections.singleton(role));
        newUser.setFavorites(Collections.emptySet());

        User savedUser = repository.save(newUser);

        return mapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    @Transactional
    public UserResponseDto setAdminRole(String email) {

        User user = repository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        if (!user.getRoles().contains("ADMIN")) {
            Set<Role> roles = user.getRoles();
            roles.add(roleService.getRoleByTitle("ADMIN"));
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
        user.setId(userId);

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
    public UserResponseDto changePassword(Long userId, String newPassword) {
        User user = findUserById(userId);
        String encodedPass = encoder.encode(newPassword);
        user.setPassword(encodedPass);
        User saved = repository.save(user);

        return mapper.map(saved, UserResponseDto.class);
    }


    @Override
    public Optional<UserResponseDto> getUserById(Long id) {

      return Optional.ofNullable(mapper.map(findUserById(id), UserResponseDto.class));
    }

    @Override
    public List<UserResponseDto> findUsersByNameOrEmail(String name, String email) {
        List<User> users = repository.findByNameContainingOrEmailContaining(name, email);

        return users.stream()
                .map(user -> mapper.map(user, UserResponseDto.class)).toList();
    }

    public UserResponseDto getUserResponseDtoByName(String name) {
        Optional<User> userOptional = repository.findUserByName(name);

        if (userOptional.isPresent()) {

            UserResponseDto dto = new UserResponseDto();
            dto.setId(userOptional.get().getId());
            dto.setName(userOptional.get().getName());
            dto.setEmail(userOptional.get().getEmail());
            List<StationResponseDto> favoriteStationDTOList = userOptional.get().getFavorites().stream()
                    .map(station -> new StationResponseDto())
                            .toList();
            dto.setFavorites(favoriteStationDTOList);
            dto.setRoles(userOptional.get().getRoles());

            return dto;

        } else {
            throw new UserNotFoundException("User with name " + name + " not found");
        }
    }

    //как spring получает User по логину - логин - это name!
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

      return repository.findUserByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + name + " not found"));
    }

    @Override
    public boolean toggleFavoriteStation(Long userId, String stationUuid) {
        User user = findUserById(userId);
        Object station = stationRepository.findByStationuuid(stationUuid)
                .orElseThrow(() -> new RuntimeException("Station not found with UUID: " + stationUuid));

        if (user.getFavorites().contains(station)) {

            user.getFavorites().remove(station);
            repository.save(user);
            return false;
        } else {
            user.getFavorites().add((Station) station);
            repository.save(user);
            return true;
        }
    }
}


