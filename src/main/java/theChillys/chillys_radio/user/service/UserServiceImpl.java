package theChillys.chillys_radio.user.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.exception.UserNotFoundException;
import theChillys.chillys_radio.security.UserResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.StationRepository;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final ModelMapper modelMapper;


   public Station findStationById(Long stationId) {
        Station station_entity = stationRepository
                .findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("Station with id" + stationId + " not found"));
        return station_entity;
    }


    public User findUserById(Long userId) {
        User user_entity = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id" + userId + " not found"));
        return user_entity;
    }


    @Override
    public UserResponseDto getUsersFavoriteStations(Long userId, Long stationId) {

        User user = findUserById(userId);

        List<StationResponseDto> favoriteStationsDto = user.getFavorites().stream()
                .map(st -> modelMapper.map(st, StationResponseDto.class))
                .collect(Collectors.toList());

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), favoriteStationsDto);
    }

    @Override
    public boolean setLike(Long userId, Long stationId) {
        return false;
    }

    @Override
    public boolean logOut(Long userId) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
