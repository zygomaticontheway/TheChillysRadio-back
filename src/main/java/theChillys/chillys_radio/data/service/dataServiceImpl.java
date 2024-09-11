package theChillys.chillys_radio.data.service;

import lombok.AllArgsConstructor;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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
    private final String getAllStationsUrl = "http://all.api.radio-browser.info/json/stations";
    private final String getStationByStationuuidUrl = "http://all.api.radio-browser.info/json/stations/byuuid/";//+ stationuuid
    private final String postClickStationUrl = "http://all.api.radio-browser.info/json/stations/byuuid/"; //+ stationuuid
    private final String postVoteStationUrl = "http://all.api.radio-browser.info/json/url/"; //+ stationuuid
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
                    String message = "Added items to database: " + modifiedItems;
                    return new ModifyResponseDto(true, message, modifiedItems);
                })
                .onErrorResume(e -> {
                    String errorMessage = "Error occurred: " + e.getMessage();
                    return Mono.just(new ModifyResponseDto(false, errorMessage, 0L));
                });
    }
    private Mono<StationResponseDto> saveStationToDatabase(DataResponseDto response) {
        return Mono.fromCallable(() -> {
            Station station = mapper.map(response, Station.class);
            Station savedStation = repository.save(station);
            return mapper.map(savedStation, StationResponseDto.class);
        });
    }

    private Mono<Long> saveListStationsToDatabase(List<DataResponseDto> response) {
        return Flux.fromIterable(response)
                .flatMap(this::saveStationToDatabase)
                .count();
    }

    @Override
    public ModifyResponseDto getStationByStationuuid(String stationuuid) {
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

}
