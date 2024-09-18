package theChillys.chillys_radio.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    UserResponseDto createUser(UserRequestDto dto);

    List<UserResponseDto> getUsers();

    UserResponseDto updateUser(Long userId, UserRequestDto dto);

    UserResponseDto changePassword(Long userId, String newPassword);

    Optional<UserResponseDto> getUserById(Long id);
    List<UserResponseDto> findUsersByNameOrEmail(String name, String email);

    UserResponseDto getUsersFavoriteStations(Long userId);

    UserResponseDto setAdminRole(String name);

    UserDetails loadUserByUsername(String name) throws UsernameNotFoundException;

    boolean toggleFavoriteStation(Long userId, String stationuuid);

    UserResponseDto getUserResponseDtoByName(String name);

}
