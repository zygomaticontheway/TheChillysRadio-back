package theChillys.chillys_radio.user.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    UserResponseDto createUser(UserRequestDto dto);

    List<UserResponseDto> getUsers();

    Optional<UserResponseDto> getUserById(Long id);

    List<UserResponseDto> findUsersByNameOrEmail(String name, String email);

    UserResponseDto getUsersFavoriteStations(Long userId);

    boolean setLike(Long userId, Long stationId);

    boolean logOut(Long userId);

    UserResponseDto setAdminRole(String username);

}
