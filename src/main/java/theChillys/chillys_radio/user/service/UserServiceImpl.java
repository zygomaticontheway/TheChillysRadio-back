package theChillys.chillys_radio.user.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.exception.UserNotFoundException;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.repository.IUserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class UserServiceImpl implements IUserService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService

    private final IUserRepository repository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;

    @Override
    public List<UserResponseDto> getUsers() {
        return List.of();
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        //проверяем существование пользователя с таким именем, если он есть, то выкидываем exception
        repository.findUserByName(dto.getName()).ifPresent(u -> {
            throw new RuntimeException("User " + dto.getName() + "already exist");
        });

//        получаем роль из базы
        Role role = roleService.getRoleByTitle("ROLE_USER");

        //Collections.singleton(role)  это добавлено в переменную при создании юзера, но это выполняет код ниже:
        //        HashSet<Role> roles = new HashSet<>();
        //        roles.add(role);

//        кодируем пароль
        String encodedPass = encoder.encode(dto.getPassword());

//        создаем пользователя
        User newUser = new User(null, dto.getName(), dto.getEmail(), encodedPass, Collections.singleton(role));

//        сохраняем его в репо
        repository.save(newUser);


        return mapper.map(newUser, UserResponseDto.class);
    }

    @Override
    public UserResponseDto setAdminRole(String username) {
        return null;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = repository.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserResponseDto.class)).toList();

    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        return Optional.ofNullable(mapper.map(findUserById(id), UserResponseDto.class));
    }

    @Override
    public User findUserById(Long id) {
        String msg = "User id:" + id + " not found";
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(msg));
    }

    @Override
    public List<User> findUsersByNameOrEmail(String name, String email) {
        IUserRepository userRepository = null;
        return userRepository.findByNameContainingOrEmailContaining(name, email);
    }

    //как spring получает User по логину
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        return repository.findUserByName(name).orElseThrow(() -> new UsernameNotFoundException("User with name: " + name + " not found"));
    }

}
