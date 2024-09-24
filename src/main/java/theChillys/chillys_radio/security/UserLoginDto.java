package theChillys.chillys_radio.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Schema(name = "Login Dto", description = "Info for login")
public class UserLoginDto {

    @Schema(description = "User login", example = "user")
    private String name;
    @Schema(description = "password", example = "qwerty")
    private String password;
}
