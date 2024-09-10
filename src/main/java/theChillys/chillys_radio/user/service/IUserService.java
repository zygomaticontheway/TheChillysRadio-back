package theChillys.chillys_radio.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {


    UserResponseDto getUsersFavoriteStations(Long userId);
    public boolean setLike(Long userId, Long stationId);
    public boolean logOut (Long userId);

    public List<UserResponseDto> getUsers();
//  public UserResponseDto getUserById(Long id);
    public UserResponseDto createUser(UserRequestDto dto);
    public UserResponseDto setAdminRole(String username);

    public  List<UserResponseDto> getAllUsers();

    public  UserResponseDto getUserById(Long id);

    public  List<UserResponseDto> findUsersByNameOrEmail(String name, String email);



    UserDetails loadUserByUsername (String name) throws UsernameNotFoundException;

}
