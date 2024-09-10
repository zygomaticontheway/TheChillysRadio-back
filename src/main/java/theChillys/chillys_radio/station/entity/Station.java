package theChillys.chillys_radio.station.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @Column(name = "stationuuid")
    private String stationuuid;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "url_resolved")
    private String url_resolved;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "favicon")
    private String favicon;

    @Column(name = "tags")
    private String tags;

    @Column(name = "country")
    private String country;

    @Column(name = "countrycode")
    private String countrycode;

    @Column(name = "state")
    private String state;

    @Column(name = "language")
    private String language;


    @Column(name = "languagecodes")
    private String languagecodes;

    @Column(name = "votes")
    private Long votes;


    @Column(name = "lastchangetime")
    private String lastchangetime;

    @Column(name = "codec")
    private String codec;

    @Column(name = "bitrate")
    private Integer bitrate;

    @Column(name = "lastcheckok")
    private int lastcheckok;

    @Column(name = "clickcount")
    private Long clickcount;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station station)) return false;

        return stationuuid.equals(station.stationuuid);
    }

    @Override
    public int hashCode() {
        return stationuuid.hashCode();
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationuuid='" + stationuuid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
