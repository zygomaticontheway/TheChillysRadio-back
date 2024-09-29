package theChillys.chillys_radio.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.exception.UserAlreadyExistsException;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.UserServiceImpl;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tags(value = {@Tag(name = "auth_controller")})
public class AuthController {

    private final AuthService authService;
    private final UserServiceImpl service;

    @Operation(summary = "Login user", description = "Login a user by their username and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto user) {
        try {
            TokenResponseDto tokens = authService.login(user);
            return ResponseEntity.ok(tokens);

        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }


    @Operation(summary = "Refresh access token", description = "Login a user by new generated access token")
    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto dto) {
        return authService.getNewAccessToken(dto.getRefreshToken());
    }

    @Operation(summary = "Logout user", description = "Log out from the system")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("Authorization", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Register new user", description = "Register for an account")
    @PostMapping("/register")
    public ResponseEntity<?> registrationUser(@Validated @RequestBody UserRequestDto user) {
        try {
            UserResponseDto newUser = service.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A user with this name already exists");
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid user data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering a user");
        }
    }

}

