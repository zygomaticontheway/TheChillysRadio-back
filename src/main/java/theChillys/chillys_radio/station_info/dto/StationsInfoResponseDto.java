package theChillys.chillys_radio.station_info.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StationsInfoResponseDto {
    private Long id;
    private String title;
    private Long amount;
    private String type;

    public StationsInfoResponseDto(String title, Long amount, String type) {
        this.title = title;
        this.amount = amount;
        this.type = type;
    }

    @Override
    public String toString() {
        return "StationInfoResponseDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                '}';
    }
}
