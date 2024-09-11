package theChillys.chillys_radio.data.service;

import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;


public interface IDataService {
    Mono<ModifyResponseDto> getAllStations();
    Mono<ModifyResponseDto> getStationByStationuuid(String stationuuid);
    Mono<ModifyResponseDto> postClickStation(String stationuuid);
    Mono<ModifyResponseDto> postVoteStation(String stationuuid);
}
