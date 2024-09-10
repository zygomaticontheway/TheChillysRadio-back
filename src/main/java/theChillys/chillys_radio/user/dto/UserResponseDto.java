package theChillys.chillys_radio.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationResponseDto;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private List<StationResponseDto> favorites;
    private Set<Role> roles;

}

