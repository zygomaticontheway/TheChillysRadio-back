package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.repository.IUserRepository;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.List;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class StationService implements IStationService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService


}
