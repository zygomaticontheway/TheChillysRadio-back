package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import theChillys.chillys_radio.user.controller.UserController;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody UserLoginDto user) {
        try {
            return service.login(user);

        } catch (AuthException e) {
            return new TokenResponseDto(null, null);
        }
    }

    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto dto){
        return service.getNewAccessToken(dto.getRefreshToken());
    }

    @PostMapping("/register")
    public UserResponseDto registrationUser(@RequestBody UserRequestDto user ) {
        UserController controller = new UserController();
        return controller.createUser(user);

    }

}
