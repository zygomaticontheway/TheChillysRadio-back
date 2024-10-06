package theChillys.chillys_radio.data.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.data.service.DNSLookup;
import theChillys.chillys_radio.data.service.IDataService;
import theChillys.chillys_radio.data.service.IRestTemplateDataService;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    @Autowired
    private final IDataService service;
    private final DNSLookup dnsLookup;
    private final IRestTemplateDataService rtDataService;

    @GetMapping("/fetch-stations")
    public ModifyResponseDto fetchAllStations (){

        return rtDataService.fetchAllStations();
    }

    @GetMapping("/ivan-stations")
    public Mono<ModifyResponseDto> getAllStations(){

        return service.getAllStations();
    }

    @GetMapping("/ivan-stations/{stationuuid}")
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

    @GetMapping("/dns-lookup")
    public String getRandomHost() {

        return dnsLookup.getRandomHost();
    }


}
