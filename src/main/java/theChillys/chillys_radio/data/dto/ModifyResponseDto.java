package theChillys.chillys_radio.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModifyResponseDto {

    private boolean ok;
    private String message;
    private String stationuuid;
    private String name;
    private String url;
}
