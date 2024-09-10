package theChillys.chillys_radio.station.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.station.repository.IStationRepository;


@RequiredArgsConstructor
@Service
public class StationServiceImpl implements IStationService {
    private final ModelMapper mapper;
    private final IStationRepository repository;

}
