package theChillys.chillys_radio.data.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.data.service.IDataService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    @Autowired
    private final IDataService service;

    @GetMapping("/all")
    public Mono<ModifyResponseDto> getAllStations(){

        return service.getAllStations();
    }

    @GetMapping("/all/{stationuuid}")
    public Mono<ModifyResponseDto> getStationByStationuuid(@PathVariable (name = "stationuuid") String stationuuid){

        return service.getStationByStationuuid(stationuuid);
    }

    @GetMapping("/vote/{stationuuid}")
    public Mono<ModifyResponseDto> vote (@PathVariable (name = "stationuuid") String stationuuid){

        return service.postVoteStation(stationuuid);
    }

    @GetMapping("/click/{stationuuid}")
    public Mono<ModifyResponseDto> click (@PathVariable (name = "stationuuid") String stationuuid){

        return service.postClickStation(stationuuid);
    }


}
