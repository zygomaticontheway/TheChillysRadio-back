package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;
    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;

    private final AuthService authService = mock(AuthService.class);

    private final UserServiceImpl userService = mock(UserServiceImpl.class);

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService, userService);
        userRequestDto = new UserRequestDto();
        userRequestDto.setName("Test User");
        userRequestDto.setEmail("test@example.com");
        userRequestDto.setPassword("password123");

        userResponseDto = new UserResponseDto();
        userResponseDto.setName("Test User");
        userResponseDto.setEmail("test@example.com");
    }

    @Test
    void login_SuccessfulLogin() throws AuthException {
        try {

            UserLoginDto loginDto = new UserLoginDto("testUser", "password123");
            TokenResponseDto expectedResponse = new TokenResponseDto("accessToken", "refreshToken");

            when(authService.login(loginDto)).thenReturn(expectedResponse);

            TokenResponseDto responseBody = authController.login(loginDto);
            ResponseEntity<TokenResponseDto> response = ResponseEntity.ok(responseBody);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(expectedResponse, response.getBody());
            verify(authService).login(loginDto);

            System.out.println("Test login_SuccessfulLogin passed successfully!");
        } catch (AssertionError | Exception e) {
            System.out.println("Test login_SuccessfulLogin failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void login_FailedLogin() throws AuthException {
        try {

            UserLoginDto loginDto = new UserLoginDto("testUser", "wrongPassword");

            when(authService.login(loginDto)).thenThrow(new AuthException("Incorrect password"));

            TokenResponseDto responseBody = authController.login(loginDto);
            ResponseEntity<TokenResponseDto> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponseDto(null, null));

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(null, response.getBody().getAccessToken());
            assertEquals(null, response.getBody().getRefreshToken());
            verify(authService).login(loginDto);

            System.out.println("Test login_FailedLogin passed successfully!");
        } catch (AssertionError | Exception e) {
            System.out.println("Test login_FailedLogin failed: " + e.getMessage());
            throw e;
        }
    }


    @Test
    void logout() {
        try {

            HttpServletResponse response = mock(HttpServletResponse.class);

            ResponseEntity<Void> actualResponse = authController.logout(response);

            assertEquals(HttpStatus.NO_CONTENT, actualResponse.getStatusCode());

            Cookie expectedCookie = new Cookie("Authorization", null);
            expectedCookie.setPath("/");
            expectedCookie.setHttpOnly(true);
            expectedCookie.setMaxAge(0);

            verify(response).addCookie(Mockito.argThat(cookie ->
                    "Authorization".equals(cookie.getName()) &&
                            cookie.getValue() == null &&
                            "/".equals(cookie.getPath()) &&
                            cookie.isHttpOnly() &&
                            cookie.getMaxAge() == 0
            ));

            System.out.println("Test logout passed successfully!");

        } catch (AssertionError e) {
            System.out.println("Test logout failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void registrationUser_SuccessfulRegistration() {

        when(userService.createUser(userRequestDto)).thenReturn(userResponseDto);

        UserResponseDto response = authController.registrationUser(userRequestDto);

        assertNotNull(response);
        assertEquals(userResponseDto.getName(), response.getName());
        assertEquals(userResponseDto.getEmail(), response.getEmail());
        System.out.println("Test registrationUser_SuccessfulRegistration passed successfully!");
    }

    @Test
    void registrationUser_UserAlreadyExists() {
        when(userService.createUser(userRequestDto)).thenThrow(new RuntimeException("User Test User already exists"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.registrationUser(userRequestDto);
        });

        assertEquals("User Test User already exists", exception.getMessage());
        System.out.println("Test registrationUser_UserAlreadyExists passed successfully!");
    }

    @Test
    public void registrationUser_EmptyFields() {
        UserRequestDto userRequestDto = new UserRequestDto();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.registrationUser(userRequestDto);
        });

        assertEquals("User name is required", exception.getMessage());
        System.out.println("Test: 'registrationUser_EmptyFields' - user name check passed successfully.");

        userRequestDto.setName("Test User");
        userRequestDto.setEmail(null);
        exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.registrationUser(userRequestDto);
        });
        assertEquals("Email is required", exception.getMessage());
        System.out.println("Test: 'registrationUser_EmptyFields' - email check passed successfully.");

        userRequestDto.setEmail("test@example.com");
        userRequestDto.setPassword(null);
        exception = assertThrows(IllegalArgumentException.class, () -> {
            authController.registrationUser(userRequestDto);
        });
        assertEquals("Password is required", exception.getMessage());
        System.out.println("Test: 'registrationUser_EmptyFields' - password check passed successfully!");
    }

}


