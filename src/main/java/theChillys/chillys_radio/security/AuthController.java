package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.user.controller.UserController;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.IUserService;
import theChillys.chillys_radio.user.service.UserServiceImpl;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserServiceImpl service;



    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody UserLoginDto user) {
        try {
            return authService.login(user);

        } catch (AuthException e) {
            return new TokenResponseDto(null, null);
        }
    }

    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto dto){
        return authService.getNewAccessToken(dto.getRefreshToken());
    }


   // @GetMapping("/logout")
   // public TokenResponseDto logout(@RequestHeader("Authorization") String authHeader) {

    //    String token = authHeader.substring(7);
    //    authService.logout(token);
    //    return new TokenResponseDto(null, null);
   // }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Очищаем куки
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // Устанавливаем время жизни куки в 0 для удаления
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/register")
    public UserResponseDto registrationUser(@RequestBody UserRequestDto user ) {
        return service.createUser(user);
    }

}
