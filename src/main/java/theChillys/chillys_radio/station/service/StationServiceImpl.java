package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.data.service.IDataService;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StationServiceImpl implements IStationService {

    private static final Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    private final ModelMapper mapper;
    private final IStationRepository repository;
    private final IDataService dataService;



    @Override
    public List<StationResponseDto> getAllStationsByTopClicks() {

        logger.debug("Fetching stations by top clicks");

        List<Station> stations = repository.findAllByOrderByClickcountDesc();
        return stations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StationResponseDto> getAllStationsByTopVotes() {

        logger.debug("Fetching stations by top votes");

        List<Station> stations = repository.findAllByOrderByVotesDesc();
        return stations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public StationResponseDto getStationByStationuuid(String stationuuid) {

        logger.debug("Fetching station");

        Station station = repository.findByStationuuid(stationuuid)
                .orElseThrow(() -> new StationNotFoundException("Station not found with uuid: " + stationuuid));
        return convertToDto(station);
    }

    private StationResponseDto convertToDto(Station station) {
        return mapper.map(station, StationResponseDto.class);
    }


    @Override
    public Mono<ModifyResponseDto> vote(String stationuuid) {
        return dataService.postVoteStation(stationuuid)
                .flatMap(result -> {
                    if (result.isOk()) {
                        logger.info(result.getMessage());

                        return dataService.getStationByStationuuid(stationuuid)
                                .doOnNext(r -> logger.debug(r.getMessage()))
                                .doOnError(err -> logger.error("Refreshing station: " + stationuuid + " failed"))
                                .then(Mono.just(result)); // return Mono<ModifyResponseDto>
                    } else {
                        return Mono.just(result); // Вернем результат голосования, даже если не `OK`
                    }
                })
                .doOnError(error -> logger.error("Voting for the station: " + stationuuid + " failed"));
    }

    @Override
    public Mono<ModifyResponseDto> click(String stationuuid) {
        return dataService.postClickStation(stationuuid)
                .flatMap(result -> {
                    if (result.isOk()) {
                        logger.info("Click registered for station: " + stationuuid);
                        return Mono.just(result); // return Mono<ModifyResponseDto>
                    } else {
                        logger.warn("Failed to register click for station: " + stationuuid);
                        return Mono.just(result); // Вернуть результат, даже если он не OK
                    }
                })
                .doOnError(error -> logger.error("Error while registering click for station: " + stationuuid, error));
    }

    @Override
    public StationUrlDto getStreamUrl(String stationuuid) {

        String urlResolved = repository.findByStationuuid(stationuuid).get().getUrl_resolved();

        return new StationUrlDto(urlResolved);
    }



    @Override
    public List<StationResponseDto> findStationsByGenreCountryLanguage(String name,String tags, String country, String language) {
        List<Station> stations = repository.findStationByGenreOrCountryOrLanguage(name, tags,  country,  language);

        return stations.stream()
                .map(station -> mapper.map(station,StationResponseDto.class))
                .toList();
    }
}


