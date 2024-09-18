package theChillys.chillys_radio.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataResponseDto {

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
    private int bitrate;
    private int lastcheckok;
    private Long clickcount;
    private String changeuuid;
    private String iso_3166_2;
    private String lastchangetime_iso8601;
    private String hls;
    private String lastchecktime;
    private String lastchecktime_iso8601;
    private String lastcheckoktime;
    private String lastcheckoktime_iso8601;
    private String lastlocalchecktime;
    private String lastlocalchecktime_iso8601;
    private String clicktimestamp;
    private String clicktimestamp_iso8601;
    private String clicktrend;
    private String ssl_error;
    private String geo_lat;
    private String geo_long;
    private String has_extended_info;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataResponseDto that)) return false;

        return stationuuid.equals(that.stationuuid);
    }

    @Override
    public int hashCode() {
        return stationuuid.hashCode();
    }

    @Override
    public String toString() {
        return "DataResponseDto{" +
                "id=" + id +
                ", stationuuid='" + stationuuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
