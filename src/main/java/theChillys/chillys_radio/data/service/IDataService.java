package theChillys.chillys_radio.data.service;

import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;


public interface IDataService {
    Mono<ModifyResponseDto> getAllStations();
    ModifyResponseDto getStationByStationuuid(String stationuuid);
    ModifyResponseDto postClickStation(String stationuuid);
    ModifyResponseDto postVoteStation(String stationuuid);
}
