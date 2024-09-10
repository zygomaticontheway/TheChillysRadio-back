package theChillys.chillys_radio.station.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;


public interface IStationService extends UserDetailsService {

    UserResponseDto createUser(UserRequestDto dto);
}
