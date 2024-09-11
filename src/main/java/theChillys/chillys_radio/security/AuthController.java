package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    //!!!!!!!!!!!!!!!!!!!!!!!!
    @GetMapping("/logout")
    public TokenResponseDto logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Предполагаем, что токен в формате "Bearer <token>"

        // Логика для выхода пользователя
        service.logout(token);

        // Возвращаем пустой `TokenResponseDto` или сообщение об успешном выходе, если необходимо
        return new TokenResponseDto(null, null);
    }

}
