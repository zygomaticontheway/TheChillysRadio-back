package theChillys.chillys_radio.station.service;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;

import java.util.List;

public interface IStationService {
    List<StationResponseDto> getAllStationsByTopClicks();

    Page<StationResponseDto> getAllStations(int page, int size);

    List<StationResponseDto> getAllStationsByTopVotes();

    StationResponseDto getStationByStationuuid(String stationuuid);

    Mono<ModifyResponseDto> vote(String stationuuid);

    Mono<ModifyResponseDto> click(String stationuuid);

    StationUrlDto getStreamUrl(String stationuuid);

    List<StationResponseDto> findStationByNameTagsCountryLanguage(String name, String tags, String country, String language);

}
