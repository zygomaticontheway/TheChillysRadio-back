package theChillys.chillys_radio.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.station.service.IStationService;

import java.lang.reflect.Field;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RestTemplateDataServiceImpl implements IRestTemplateDataService {

    private final DNSLookup dnsLookup;
    private final ModelMapper mapper;
    private final IStationRepository repository;
//    private final IStationService stationsService;

    @Override
    public ModifyResponseDto fetchAllStations() {
        ModifyResponseDto result = new ModifyResponseDto(false, "!!!!!! Fetch stations haven't been started.", 0L);
        Integer offset = 0;
        Integer limit = 30000;
        String host = dnsLookup.getRandomHost();
        Long savedAmount = 0L;

        while (true) {
            List<DataResponseDto> fetchedStations = fetchStations(offset, limit, host);

            if (fetchedStations == null || fetchedStations.isEmpty()) {
//                System.out.println("!-!-!-! Response array is empty. Fetching finished");
                break;
            }
                savedAmount = savedAmount + saveListStationsToDatabase(fetchedStations);
                offset  = offset+1;

//                System.out.println(offset);

                result.setOk(true);
                result.setMessage("++SUCCESS++ Response array is empty. Fetching finished");
                result.setModifiedItems(savedAmount);
        }
                return result;
    }

    @Override
    public ModifyResponseDto fetchStationByStationuuid(String stationuuid) {
        return null;
    }

    @Override
    public ModifyResponseDto postClickStation(String stationuuid) {
        return null;
    }

    @Override
    public ModifyResponseDto postVoteStation(String stationuuid) {
        return null;
    }

    private List<DataResponseDto> fetchStations(Integer offset, Integer limit, String host) {
        RestTemplate restTemplate = new RestTemplate();
        String URL = "http://" + host + "/json/stations";

        String urlWithParams = String.format("%s?offset=%d&limit=%d", URL, offset, limit );
//        System.out.println("----URL----: " + urlWithParams);

        ResponseEntity<DataResponseDto[]> response = restTemplate.getForEntity(urlWithParams, DataResponseDto[].class);
//        System.out.println("Response body: " + Arrays.toString(response.getBody()));

        DataResponseDto[] stations = response.getBody();
        List<DataResponseDto> receivedStations = new ArrayList<>();

        if (stations == null || stations.length == 0) {
//            System.out.println("Fetching stations returned empty array.");
            return receivedStations;
        }
        receivedStations = Arrays.stream(stations).toList();

        return receivedStations;
    }

    private StationResponseDto saveOrUpdateStationToDatabase(DataResponseDto dataResponseDto) {

        Station station = mapper.map(dataResponseDto, Station.class);

        if (isValidStation(station)) {

            Optional<Station> existingStationOpt = repository.findByStationuuid(station.getStationuuid());

            Station savedStation;

            if (existingStationOpt.isPresent()) {
                // Update existing station
                Station existingStation = existingStationOpt.get();
                savedStation = repository.save(existingStation);
            } else {
                // Save new station
                savedStation = repository.save(station);
            }
            return mapper.map(savedStation, StationResponseDto.class);
        } else {
            return null;
        }
    }

    private Long saveListStationsToDatabase(List<DataResponseDto> response) {

        return response.stream()
                .map(this::saveOrUpdateStationToDatabase)
                .filter(Objects::nonNull)
                .count();
    }

    private boolean isValidStation(Station station) {
        for (Field field : station.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(station);
                if (value instanceof String && ((String) value).length() > 255) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
