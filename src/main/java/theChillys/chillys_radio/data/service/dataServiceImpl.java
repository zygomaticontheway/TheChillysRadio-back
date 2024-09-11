package theChillys.chillys_radio.data.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.data.dto.DataResponseDto;
import theChillys.chillys_radio.data.dto.ModifyResponseDto;

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


@AllArgsConstructor
@Service

public class dataServiceImpl implements IDataService{

    private final ModelMapper mapper;
    private final String getAllStationsUrl = "http://all.api.radio-browser.info/json/stations";
    private final String getStationByStationuuidUrl = "http://all.api.radio-browser.info/json/stations/byuuid/";//+ stationuuid
    private final String postClickStationUrl = "http://all.api.radio-browser.info/json/stations/byuuid/"; //+ stationuuid
    private final String postVoteStationUrl = "http://all.api.radio-browser.info/json/url/"; //+ stationuuid
// TODO   private final ExecutorService executor = Executors.newCachedThreadPool(10);


    @Override
    public ModifyResponseDto getAllStations() {

        /*
 		// запрос зашел сюда
 		// генерируешь UUID и записываешь в базу задачу с этим UUID и назначаешь "ей статус в обработке""
 		// и запрос на сторонний сервис выполняется в отдельном потоке*/
        /*
 		executor.submit( () -> {
 			try {
 				// Выполнение GET-запроса
 				List<YourEntity> response = webClient.get()
 					.uri("http://your-api-endpoint.com/api/data")
 					.retrieve().bodyToFlux(YourEntity.class)
 					.collectList().block();
 				// Блокировка потока до завершения
 				if (response != null) {
 					saveToDatabase(response);
 					// таске назначешь статус - выполнено
 					// оповещаешь фронт о том, что задача выполнена
 				}
 			} catch (Exception e) {
 				// log.error()
 				// оповещаешь фронт о том, что что-то пошло не так - назначаешь таске статус - ошибка
 				throw new IllegalStateException(e);
 			}
 		}
 		// запрос от фронта продолжается тут, без провисания
 		return ResponseDto.body("Ваша задача принята").taskId(случайный номер).build();
 	}
 }
 1. вариант - websockets
 2. сделать endpoint GET /tasks/{task-id}/state
         */


        return null;
    }

    @Override
    public ModifyResponseDto getStationByStationuuid(String stationuuid) {
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
}
