package theChillys.chillys_radio.station.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Station {
  @Id
  @GeneratedValue()
  private Long id;
  private String stationuuid;
  private String name;
  private String url;
  private String url_resolved;
  private String homepage;
  private String favicon;
  private String tags;
  private String country;
  private String countryCody;
  private String state;
  private String language;
  private String languageCodes;
  private Long votes;
  private String lastChangeTime;
  private String codec;
  private int bitrate;
  private int lastCheckOk;
  private Long clickCount;

}
