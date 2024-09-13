package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import theChillys.chillys_radio.exception.StationNotFoundException;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class StationServiceImpl implements IStationService { 

    private final ModelMapper mapper;
    private final IStationRepository repository;
   // private final WebClient webClient;


    @Override
    public List<StationResponseDto> getAllStationsByTopClicks() {
        List<Station> stations = repository.getStationsByTopClicks();
        return stations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StationResponseDto> getAllStationsByTopVotes() {
        List<Station> stations = repository.getStationsByTopVotesAtSource();
        return stations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

  /*  @Override
    public StationResponseDto getStationById(String stationuuid) {
        String apiUrl = "http://all.api.radio-browser.info/json/stations/" + stationuuid;

        return webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(Station.class)
                .map(this::convertToDto)
                .onErrorResume(e -> Mono.error(new StationNotFoundException("Station not found with uuid: " + stationuuid)))
                .block(); //для синхронного результата
    }*/

    private StationResponseDto convertToDto(Station station) {
        return mapper.map(station, StationResponseDto.class);
    }
}
