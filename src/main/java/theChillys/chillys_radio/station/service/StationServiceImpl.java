package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.user.repository.IUserRepository;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class StationServiceImpl implements IStationService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService

    private final ModelMapper mapper;
    private final IStationRepository repository;

}
