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

    Page<StationResponseDto> getAllStations(int page, int size);

    Page<StationResponseDto> getAllStationsByTopClicks(Pageable pageable);

    Page<StationResponseDto> getAllStationsByTopVotes(Pageable pageable);

    Page<StationResponseDto> getStationsWithFilters(String name, String tags, String country, String language, Pageable pageable);

    StationResponseDto getStationByStationuuid(String stationuuid);

    Page<StationResponseDto> searchStationsByTerm (String search, Pageable pageable);

    Mono<ModifyResponseDto> vote(String stationuuid);

    Mono<ModifyResponseDto> click(String stationuuid);

    StationUrlDto getStreamUrl(String stationuuid);

    Map<String, Long> getTagsWithStationCount();

    Map<String, Long> getCountriesWithStationCount();

    Map<String, Long> getLanguagesWithStationCount();

    Integer getAllStationsAmount();


}
