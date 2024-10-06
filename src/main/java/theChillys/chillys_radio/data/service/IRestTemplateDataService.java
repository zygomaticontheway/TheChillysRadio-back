package theChillys.chillys_radio.data.service;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;

public interface IRestTemplateDataService {

    Page<ModifyResponseDto> getAllStations();
    Mono<ModifyResponseDto> getStationByStationuuid(String stationuuid);
    Mono<ModifyResponseDto> postClickStation(String stationuuid);
    Mono<ModifyResponseDto> postVoteStation(String stationuuid);
}
