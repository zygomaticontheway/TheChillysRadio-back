package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.data.service.IDataService;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.util.LinkedHashMap;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class StationServiceImpl implements IStationService {

    private static final Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    private final ModelMapper mapper;
    private final IStationRepository repository;
    private final IDataService dataService;

    private StationResponseDto convertToDto(Station station) {
        return mapper.map(station, StationResponseDto.class);
    }

    @Override
    public Page<StationResponseDto> getAllStations(int page, int size) {
        logger.debug("Fetching stations with page {} and size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Station> stationsPage = repository.findAll(pageable);
        return stationsPage.map(this::convertToDto);
    }

    @Override
    public Page<StationResponseDto> getAllStationsByTopClicks(Pageable pageable) {

        Page<Station> stationsPage;

        logger.debug("Fetching stations by top clicks");

        stationsPage = repository.findAllByOrderByClickcountDesc(pageable);

        return stationsPage.map(this::convertToDto);
    }

    @Override
    public Page<StationResponseDto> getAllStationsByTopVotes(Pageable pageable) {

        Page<Station> stationsPage;

        logger.debug("Fetching stations by top votes");

        stationsPage = repository.findAllByOrderByVotesDesc(pageable);

        return stationsPage.map(this::convertToDto);
    }

    @Override
    public Page<StationResponseDto> getStationsWithFilters(String name, String tags, String country, String language, Pageable pageable) {

        Page<Station> stationsPage;

        if ((name == null || name.isEmpty()) && (tags == null || tags.isEmpty()) &&
                (country == null || country.isEmpty()) && (language == null || language.isEmpty())) {

            stationsPage = repository.findAll(pageable);

            return stationsPage.map(this::convertToDto);
        }
        stationsPage = repository.findByNameContainingIgnoreCaseAndTagsContainingIgnoreCaseAndCountryContainsIgnoreCaseAndLanguageContainingIgnoreCase(
                name == null ? "" : name,
                tags == null ? "" : tags,
                country == null ? "" : country,
                language == null ? "" : language,
                pageable);

        return stationsPage.map(this::convertToDto);
    }

    @Override
    public Page<StationResponseDto> searchStationsByTerm(String search, Pageable pageable) {

        Page<Station> stationsPage = repository.searchStationsByTerm(search, pageable);

        return stationsPage.map(this::convertToDto);
    }

    @Override
    public StationResponseDto getStationByStationuuid(String stationuuid) {

        logger.debug("Fetching station");

        Station station = repository.findByStationuuid(stationuuid)
                .orElseThrow(() -> new StationNotFoundException("Station not found with uuid: " + stationuuid));

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
        click(stationuuid);

        return new StationUrlDto(urlResolved);
    }

    @Override
    public Integer getAllStationsAmount() {
        return repository.findAll().size();
    }
}


