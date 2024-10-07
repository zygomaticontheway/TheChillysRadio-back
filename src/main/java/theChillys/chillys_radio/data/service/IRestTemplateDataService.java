package theChillys.chillys_radio.data.service;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;

public interface IRestTemplateDataService {

    ModifyResponseDto fetchAllStations();
    ModifyResponseDto fetchStationByStationuuid(String stationuuid);
    ModifyResponseDto postClickStation(String stationuuid);
    ModifyResponseDto postVoteStation(String stationuuid);
}
