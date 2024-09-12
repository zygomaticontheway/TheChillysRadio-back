package theChillys.chillys_radio.data.service;

import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;

import java.util.List;

public interface IDataService {
    ModifyResponseDto getAllStations();
    ModifyResponseDto getStationByStationuuid(String stationuuid);
    ModifyResponseDto postClickStation(String stationuuid);
    ModifyResponseDto postVoteStation(String stationuuid);
}
