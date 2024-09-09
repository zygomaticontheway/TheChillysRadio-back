package theChillys.chillys_radio.user.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import theChillys.chillys_radio.security.UserResponseDto;


public interface IUserService extends UserDetailsService {

    public UserResponseDto getUsersFavoriteStations (Long userId, Long stationId);
    public boolean setLike(Long userId, Long stationId);
    public boolean logOut(Long userId);

}
