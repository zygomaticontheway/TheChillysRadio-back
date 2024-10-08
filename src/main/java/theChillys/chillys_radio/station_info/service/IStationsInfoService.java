package theChillys.chillys_radio.station_info.service;

import theChillys.chillys_radio.station_info.dto.StationsInfoResponseDto;

import java.util.List;
import java.util.Map;

public interface IStationsInfoService {
    Map<String, Long> extractFieldsToDb ();
    List<StationsInfoResponseDto> getStationsInfo ();
}
