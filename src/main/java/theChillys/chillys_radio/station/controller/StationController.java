package theChillys.chillys_radio.station.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.service.IStationService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StationController {

    @Autowired
    private IStationService service;

    @GetMapping("/stations/top-clics")
    public ResponseEntity<List<StationResponseDto>> getTopClickStations() {
        return ResponseEntity.ok(service.getAllStationsByTopClicks());
    }

    @GetMapping("/stations/top-votes")
    public ResponseEntity<List<StationResponseDto>> getTopVoteStations() {
        return ResponseEntity.ok(service.getAllStationsByTopVotes());
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<StationResponseDto> getStationById(@PathVariable String stationuuid) {
        return ResponseEntity.ok(service.getStationById(stationuuid));
    }*/
}
