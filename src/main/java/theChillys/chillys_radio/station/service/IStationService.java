package theChillys.chillys_radio.station.service;


import org.springframework.data.jpa.repository.Query;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;

import java.util.List;

public interface IStationService {
    List<StationResponseDto> getAllStationsByTopClicks();

    List<StationResponseDto> getAllStationsByTopVotes();

    StationResponseDto getStationByStationuuid(String stationuuid);

    Mono<ModifyResponseDto> vote(String stationuuid);
    Mono<ModifyResponseDto> click(String stationuuid);

    StationUrlDto getStreamUrl (String stationuuid);


    List<StationResponseDto> findStationsByGenreCountryLanguage(String genre, String country, String language);
}
