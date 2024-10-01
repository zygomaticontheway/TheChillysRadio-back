package theChillys.chillys_radio.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.exception.UserNotFoundException;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.role.Role;
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
    public List<UserResponseDto> getUsers() {
        List<User> customers = repository.findAll();

        return customers.stream().map(c -> mapper.map(c, UserResponseDto.class)).toList();
    }


    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new IllegalArgumentException("User name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

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
    public UserResponseDto setAdminRole(String name) {

        User user = repository.findUserByName(name).orElseThrow(() -> new UserNotFoundException("User with email: " + name + " not found"));

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

        User user = repository.findUserByName(name).orElseThrow(() -> new UserNotFoundException("User with name: " + name + " not found"));

        return mapper.map(user, UserResponseDto.class);

    }

    //как spring получает User по логину - логин - это name!
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        return repository.findUserByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + name + " not found"));
    }

    @Override
    public List<StationResponseDto> getUsersFavoriteStations(String name) {

        UserResponseDto user = getUserResponseDtoByName(name);

        System.out.println("?????----- founded user in getUsersFavoriteStations: " + user);
        List<StationResponseDto> favoriteStationsDto = user.getFavorites();
        System.out.println("?????----- his stations getUsersFavoriteStations: " + favoriteStationsDto);

        return favoriteStationsDto;
    }

    @Override
    public List<StationResponseDto> toggleFavoriteStation(String name, String stationuuid) {

        User user = repository.findUserByName(name).orElseThrow(() -> new UsernameNotFoundException("User with name: " + name + " not found"));
        Station station = stationRepository.findByStationuuid(stationuuid).orElseThrow(() -> new StationNotFoundException("Station with stationuuid: " + stationuuid + " not exist"));

        if (user.getFavorites().contains(station)) {
            System.out.println("-------Favorite stations BEFORE remove:" + user.getFavorites());
            user.getFavorites().remove(station);
            System.out.println("-------Favorite stations AFTER remove:" + user.getFavorites());
        } else {
            System.out.println("--+++--Favorite stations BEFORE add:" + user.getFavorites());
            user.getFavorites().add(station);
            System.out.println("--+++--Favorite stations AFTER add:" + user.getFavorites());
        }
        repository.save(user);
        return getUsersFavoriteStations(name);
    }
}
