package theChillys.chillys_radio.station.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StationRequestDto {

    private String stationuuid;
    private String name;
    private String url;
    private String urlResolved;
    private String homepage;
    private String favicon;
    private String tags;
    private String country;
    private String countrycode;
    private String state;
    private String language;
    private String languagecodes;
    private Long votes;
    private String lastchangetime;
    private String codec;
    private Integer bitrate;
    private Integer lastcheckok;
    private Long clickcount;
}
