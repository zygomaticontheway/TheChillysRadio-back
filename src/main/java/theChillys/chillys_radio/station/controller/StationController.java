package theChillys.chillys_radio.station.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.dto.StationUrlDto;
import theChillys.chillys_radio.station.service.IStationService;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class StationController {

    @Autowired
    private final IStationService service;

    @GetMapping("/stations")   //example: GET /api/stations?page=1&size=20 or /api/stations?page=2
    public ResponseEntity<Page<StationResponseDto>> getAllStations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<StationResponseDto> stations = service.getAllStations(page, size);
        return ResponseEntity.ok(stations);
    }

    @GetMapping("/stations/filtered")   //example :GET /api/search?country=finland&language=german
    public ResponseEntity<Page<StationResponseDto>> getStationsWithFilters(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "tags", required = false) String tags,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "language", required = false) String language,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StationResponseDto> stationPage = service.getStationsWithFilters(name, tags, country, language, pageable);
        return ResponseEntity.ok(stationPage);
    }

    @GetMapping("stations/search")
    public ResponseEntity<Page<StationResponseDto>> searchStations(
            @RequestParam(name = "search", required = true) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (search.isEmpty()) {
            List<StationResponseDto> emptyList = Collections.emptyList();
            Page<StationResponseDto> emptyPage = new PageImpl<>(emptyList, pageable, 0);
            return ResponseEntity.ok(emptyPage);
        }
        Page<StationResponseDto> stationPage = service.searchStationsByTerm(search, pageable);

        return ResponseEntity.ok(stationPage);
    }


    @GetMapping("/stations/top-clicks")
    public ResponseEntity<Page<StationResponseDto>> getTopClickStations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllStationsByTopClicks(pageable));
    }

    @GetMapping("/stations/top-votes")
    public ResponseEntity<Page<StationResponseDto>> getTopVoteStations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllStationsByTopVotes(pageable));
    }

    @GetMapping("/stations/{id}")
    public ResponseEntity<StationResponseDto> getStationById(@PathVariable("id") String stationuuid) {
        return ResponseEntity.ok(service.getStationByStationuuid(stationuuid));
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<String> handleStationNotFound(StationNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stations/{id}/stream")
    public StationUrlDto getStreamUrl(@PathVariable ("id") String stationuuid) {
        return service.getStreamUrl(stationuuid);
    }

    @PostMapping("/stations/{id}/vote")
    public Mono<ModifyResponseDto> vote(@PathVariable(name = "id") String stationuuid) {
        return service.vote(stationuuid); //Spring WebFlux сам обработает Mono и вернет результат клиенту асинхронно.
    }

    @GetMapping("/stations/tags")
    public ResponseEntity<Map<String, Long>> getTopTags() {
        Map<String, Long> topTags = service.getTagsWithStationCount();
        return ResponseEntity.ok(topTags);
    }

    @GetMapping("/stations/countries")
    public ResponseEntity<Map<String, Long>> getTopСountries() {
        Map<String, Long> topСountries = service.getCountriesWithStationCount();
        return ResponseEntity.ok(topСountries);
    }

    @GetMapping("/stations/languages")
    public ResponseEntity<Map<String, Long>> getTopcountriesLanguages() {
        Map<String, Long> topLanguages = service.getLanguagesWithStationCount();
        return ResponseEntity.ok(topLanguages);
    }

    @GetMapping ("/stations/amount")
    public Integer getAllStationsAmount (){
        return service.getAllStationsAmount();
    }
}
