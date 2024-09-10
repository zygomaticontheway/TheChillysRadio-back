package theChillys.chillys_radio.data.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//http://all.api.radio-browser.info/json/stations/byuuid/{searchterm}
//http://all.api.radio-browser.info/json/stations/byname/{searchterm}
//http://all.api.radio-browser.info/json/stations/bynameexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycodec/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycodecexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountry/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountryexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bycountrycodeexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bystate/{searchterm}
//http://all.api.radio-browser.info/json/stations/bystateexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bylanguage/{searchterm}
//http://all.api.radio-browser.info/json/stations/bylanguageexact/{searchterm}
//http://all.api.radio-browser.info/json/stations/bytag/{searchterm}
//http://all.api.radio-browser.info/json/stations/bytagexact/{searchterm}
//

@RequiredArgsConstructor
@AllArgsConstructor
@Service

public class dataServiceImpl implements IDataService {

    private final ModelMapper mapper;
    private final WebClient webClient;
    private final String getAllStationsUrl = "http://all.api.radio-browser.info/json/stations";
    private final String getStationByStationuuidUrl = "http://all.api.radio-browser.info/json/stations/byuuid/";//+ stationuuid
    private final String postClickStationUrl = "http://all.api.radio-browser.info/json/stations/byuuid/"; //+ stationuuid
    private final String postVoteStationUrl = "http://all.api.radio-browser.info/json/url/"; //+ stationuuid
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final IStationRepository repository;


    @Override
    public List<DataResponseDto> getAllStations() {

        List<DataResponseDto> response = new ArrayList<>();

        executor.submit(() -> {
            try {
                response = webClient.get()
                        .uri(getAllStationsUrl)
                        .retrieve()
                        .bodyToFlux(DataResponseDto.class)
                        .collectList()
                        .subscribe(list -> saveToDatabase((DataResponseDto) list));

            } catch (Exception e) {
                // log.error()
                // оповещаешь фронт о том, что что-то пошло не так - назначаешь таске статус - ошибка
                throw new IllegalStateException(e);
            }
        });
        return response;
    }

    @Override
    public DataResponseDto getStationByStationuuid(String stationuuid) {
        return null;
    }

    @Override
    public ModifyResponseDto postClickStation(String stationuuid) {
        return null;
    }

    @Override
    public ModifyResponseDto postVoteStation(String stationuuid) {
        return null;
    }

    public boolean saveToDatabase(DataResponseDto response) {

    }
// 1. вариант - websockets
// 2. сделать endpoint GET /tasks/{task-id}/state


}
