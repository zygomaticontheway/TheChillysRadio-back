package theChillys.chillys_radio.station.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;
import theChillys.chillys_radio.station.entity.Station;

import java.util.List;
import java.util.Map;

public interface IStationService {
    List<StationResponseDto> getAllStationsByTopClicks();

    Page<StationResponseDto> getAllStations(int page, int size);

    List<StationResponseDto> getAllStationsByTopVotes();

    StationResponseDto getStationByStationuuid(String stationuuid);

    Mono<ModifyResponseDto> vote(String stationuuid);

    Mono<ModifyResponseDto> click(String stationuuid);

    StationUrlDto getStreamUrl(String stationuuid);

    Page<Station> getStationsWithFilters(String name, String tags, String country, String language, Pageable pageable);

    Map<String, Long> getTagsWithStationCount();

    Map<String, Long> getCountriesWithStationCount();

    Map<String, Long> getLanguagesWithStationCount();



}
