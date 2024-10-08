package theChillys.chillys_radio.station_info.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.station_info.dto.StationsInfoResponseDto;
import theChillys.chillys_radio.station_info.entity.StationsInfo;
import theChillys.chillys_radio.station_info.repository.IStationsInfoRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StationsInfoService implements IStationsInfoService {
    private final IStationRepository stationRepository;
    private final IStationsInfoRepository repository;
    private final ModelMapper mapper;


    @Override
    public Map<String, Long> extractFieldsToDb() {
        Map<String, Long> result = new HashMap<>();
        try {
            List<StationsInfo> countriesInfos = repository.saveAll(extractCountriesWithStationCount());
            result.put("countriesInfos", Long.valueOf(countriesInfos.size()));
            List<StationsInfo> languagesInfos = repository.saveAll(extractLanguagesWithStationCount());
            result.put("languagesInfos", Long.valueOf(languagesInfos.size()));
            List<StationsInfo> stationsInfos = repository.saveAll(extractTagsWithStationCount());
            result.put("stationsInfos", Long.valueOf(stationsInfos.size()));

        } catch (RuntimeException e) {
            e.getMessage();
//            return result;
        }
        return result;
    }

    private List<StationsInfo> extractTagsWithStationCount() {
        List<Station> stations = stationRepository.findAll();

        Map<String, Long> tagCounts = stations.stream()
                .flatMap(station -> Arrays.stream(station.getTags().split(",")))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));

        return tagCounts.entrySet().stream()
                .map(entry -> new StationsInfo(entry.getKey(), entry.getValue(), "tag"))
                .collect(Collectors.toList());
    }

    private List<StationsInfo> extractCountriesWithStationCount() {
        List<Station> stations = stationRepository.findAll();

        Map<String, Long> countriesCounts = stations.stream()
                .map(Station::getCountry)
                .filter(country -> country != null && !country.trim().isEmpty())
                .collect(Collectors.groupingBy(country -> country, Collectors.counting()));


        return countriesCounts.entrySet().stream()
                .map(entry -> new StationsInfo(entry.getKey(), entry.getValue(), "country"))
                .collect(Collectors.toList());
    }

    public List<StationsInfo> extractLanguagesWithStationCount() {
        List<Station> stations = stationRepository.findAll();

        Map<String, Long> languagesCounts = stations.stream()
                .flatMap(station -> Arrays.stream(station.getLanguage().split("[, /]")))
                .map(String::trim)
                .filter(language -> !language.isEmpty())
                .collect(Collectors.groupingBy(language -> language, Collectors.counting()));

        return languagesCounts.entrySet().stream()
                .map(entry -> new StationsInfo(entry.getKey(), entry.getValue(), "language"))
                .collect(Collectors.toList());
    }

    @Override
    public List<StationsInfoResponseDto> getStationsInfo() {

        return repository.findAll().stream().
                map(e -> mapper.map(e, StationsInfoResponseDto.class))
                .toList();
    }
}
