package theChillys.chillys_radio.station.service;


import org.springframework.data.jpa.repository.Query;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;

import java.util.List;

public interface IStationService {
    List<StationResponseDto> getAllStationsByTopClicks();

    List<StationResponseDto> getAllStationsByTopVotes();

    StationResponseDto getStationById(String stationuuid);

}
