package theChillys.chillys_radio.station.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import theChillys.chillys_radio.station.service.IStationService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StationController {

    @Autowired
    private IStationService service;


}
