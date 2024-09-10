package theChillys.chillys_radio.station.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StationResponseDto {

    private Long id;
    private String stationuuid;
    private String name;
    private String url;
    private String url_resolved;
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StationResponseDto that)) return false;

        return stationuuid.equals(that.stationuuid);
    }

    @Override
    public int hashCode() {
        return stationuuid.hashCode();
    }

    @Override
    public String toString() {
        return "StationResponseDto{" +
                "id=" + id +
                ", stationuuid='" + stationuuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}


