package theChillys.chillys_radio.station.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(StationServiceImpl.class);
    private final ModelMapper mapper;
    private final IStationRepository repository;



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
    public StationResponseDto getStationById(String stationuuid) {

        logger.debug("Fetching station");

        Station station = repository.findByStationuuid(stationuuid)
                .orElseThrow(() -> new StationNotFoundException("Station not found with uuid: " + stationuuid));
        return convertToDto(station);
    }

    private StationResponseDto convertToDto(Station station) {
        return mapper.map(station, StationResponseDto.class);
    }
}
