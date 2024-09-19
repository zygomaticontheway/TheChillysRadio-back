package theChillys.chillys_radio.data.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;


//http://all.api.radio-browser.info/json/stations/byuuid/{searchterm}
//http://all.api.radio-browser.info/json/stations/byname/{searchterm}
//http://all.api.radio-browser.info/json/stations/bynameexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycodec/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycodecexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountry/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountryexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountrycodeexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bystate/{searchterm}
//http://all.api.radio-browser.info/json/stations/bystateexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bylanguage/{searchterm}
//http://all.api.radio-browser.info/json/stations/bylanguageexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bytag/{searchterm}
//http://all.api.radio-browser.info/json/stations/bytagexact/{searchterm}
//

@RequiredArgsConstructor
@Service

public class dataServiceImpl implements IDataService {

    private final ModelMapper mapper;
    private final WebClient webClient;
    private final String getAllStationsUrl = "http://all.api.radio-browser.info/json/stations?hidebroken=true&limit=1000";
    private final String getStationByStationuuidUrl = "http://all.api.radio-browser.info/json/stations/byuuid/";//+ stationuuid
    private final String postClickStationUrl = "http://all.api.radio-browser.info/json/url/"; //+ stationuuid
    private final String postVoteStationUrl = "http://all.api.radio-browser.info/json/vote/"; //+ stationuuid
    private final IStationRepository repository;


    @Override
    public Mono<ModifyResponseDto> getAllStations() {
        return webClient.get()
                .uri(getAllStationsUrl)
                .retrieve()
                .bodyToFlux(DataResponseDto.class)
                .collectList()
                .flatMap(this::saveListStationsToDatabase)
                .map(modifiedItems -> {
                    String message = "Added or updated items in database: " + modifiedItems;
                    return new ModifyResponseDto(true, message, modifiedItems);
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error occurred: " + e.getMessage();
                    return Mono.just(new ModifyResponseDto(false, errorMessage, 0L));
                });
    }

    private Mono<StationResponseDto> saveOrUpdateStationToDatabase(DataResponseDto dataResponseDto) {
        return Mono.fromCallable(() -> {

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
        });
    }

    private Mono<Long> saveListStationsToDatabase(List<DataResponseDto> response) {
        return Flux.fromIterable(response)
                .flatMap(this::saveOrUpdateStationToDatabase)
                .filter(station -> station != null)
                .count();
    }

    @Override
    public Mono<ModifyResponseDto> getStationByStationuuid(String stationuuid) {
        return webClient.get()
                .uri(getStationByStationuuidUrl + stationuuid)
                .retrieve()
                .bodyToMono(DataResponseDto.class)
                .flatMap(this::saveOrUpdateStationToDatabase)
                .map(savedStation -> {
                    String message = "Station retrieved and saved/updated: " + savedStation.getStationuuid();
                    return new ModifyResponseDto(true, message, 1L);
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error occurred while fetching station: " + e.getMessage();
                    return Mono.just(new ModifyResponseDto(false, errorMessage, 0L));
                })
                .switchIfEmpty(Mono.just(new ModifyResponseDto(false, "Station not saved due to invalid field length", 0L)));
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

    @Override
    public Mono<ModifyResponseDto> postClickStation(String stationuuid) {
        return webClient.post()
                .uri(postClickStationUrl + stationuuid)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    String message = "Click registered for station: " + stationuuid;
                    return new ModifyResponseDto(true, message, 1L);
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error registering click: " + e.getMessage();
                    return Mono.just(new ModifyResponseDto(false, errorMessage, 0L));
                });
    }

    @Override
    public Mono<ModifyResponseDto> postVoteStation(String stationuuid) {
        return webClient.post()
                .uri(postVoteStationUrl + stationuuid)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    String message = "Vote registered for station: " + stationuuid;
                    return new ModifyResponseDto(true, message, 1L);
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error registering vote: " + e.getMessage();
                    return Mono.just(new ModifyResponseDto(false, errorMessage, 0L));
                });
    }
}