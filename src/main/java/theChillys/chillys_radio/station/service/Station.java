package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class Station implements IUserService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService

    private final IUserRepository repository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;


    @Override
    public UserResponseDto getUsersFavoriteStations(Long userId, Long stationId) {
        return null;
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return List.of();
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto setAdminRole(String username) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
