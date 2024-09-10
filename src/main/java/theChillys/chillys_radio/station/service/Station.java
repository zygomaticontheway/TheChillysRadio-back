package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IUserRepository;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class Station implements IUserService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService

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
        if (dto == null || dto.getName() == null || dto.getUrl() == null){
            throw new IllegalArgumentException("Error: Name and URL are required");
        }
        User user = new User();

        User savedUser = repository.save(user);

        return (UserResponseDto) repository;
    }

    @Override
    public UserResponseDto setAdminRole(String username) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = null;
        if (user == null) {
            throw new UsernameNotFoundException("User is not found with username: " + username);
        }

        User user = repository.findUserByName(username);

        return user;
    }
}
