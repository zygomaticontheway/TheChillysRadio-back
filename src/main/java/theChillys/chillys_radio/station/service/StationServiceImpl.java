package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


//    public List<StationResponseDto> getAllStations() {
//
//        logger.debug("Fetching all stations ");
//
//        List<Station> stations = repository.findAll();
//        return stations.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<StationResponseDto> getAllStationsByTopClicks() {

        logger.debug("Fetching stations by top clicks");

        List<Station> stations = repository.findAllByOrderByClickcountDesc();
        return stations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<StationResponseDto> getAllStations(int page, int size) {
        logger.debug("Fetching stations with page {} and size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Station> stationsPage = repository.findAll(pageable);
        return stationsPage.map(this::convertToDto);
    }

    private StationResponseDto convertToDto(Station station) {
        return mapper.map(station, StationResponseDto.class);
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

        System.out.println("+++ founded station" + station);

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
    public Page<Station> getStationsWithFilters(String name, String tags, String country, String language, Pageable pageable) {
        if ((name == null || name.isEmpty()) && (tags == null || tags.isEmpty()) &&
                (country == null || country.isEmpty()) && (language == null || language.isEmpty())) {
            return repository.findAll(pageable);

        }
        return repository.findByNameContainingIgnoreCaseAndTagsContainingIgnoreCaseAndCountryContainsIgnoreCaseAndLanguageContainingIgnoreCase(
                name == null ? "" : name,
                tags == null ? "" : tags,
                country == null ? "" : country,
                language == null ? "" : language,
                pageable);

    }

    @Override
    public Map<String, Long> getTagsWithStationCount() {
        List<Station> stations = repository.findAll();

        Map<String, Long> tagCounts = stations.stream()
                .flatMap(station -> Arrays.stream(station.getTags().split(",")))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));

        return tagCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


    @Override
    public Map<String, Long> getCountriesWithStationCount() {
        List<Station> stations = repository.findAll();

        Map<String, Long> countriesCounts = stations.stream()
                .map(station -> station.getCountry())
                .filter(country -> country != null && !country.trim().isEmpty())
                .collect(Collectors.groupingBy(country -> country, Collectors.counting()));

        return countriesCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }



    @Override
    public Map<String, Long> getLanguagesWithStationCount() {
        List<Station> stations = repository.findAll();

        Map<String, Long> languagesCounts = stations.stream()
                .flatMap(station -> Arrays.stream(station.getLanguage().split("[, /]")))
                .map(String::trim)
                .filter(language -> !language.isEmpty())
                .collect(Collectors.groupingBy(language -> language, Collectors.counting()));

        return languagesCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }



}


