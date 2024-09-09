package theChillys.chillys_radio.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    public List<UserResponseDto> getUsers();
//    public UserResponseDto getUserById(Long id);
    public UserResponseDto createUser(UserRequestDto dto);
    public UserResponseDto setAdminRole(String username);

    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    List<User> findUsersByNameOrEmail(String name, String email);
}
