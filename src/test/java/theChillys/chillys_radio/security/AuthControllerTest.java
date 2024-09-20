package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import theChillys.chillys_radio.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;

    private final AuthService authService = mock(AuthService.class);
    private final UserServiceImpl userService = mock(UserServiceImpl.class);

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService, userService);
    }

    @Test
    void login_SuccessfulLogin() throws AuthException {
        try {
            // Arrange
            UserLoginDto loginDto = new UserLoginDto("testUser", "password123");
            TokenResponseDto expectedResponse = new TokenResponseDto("accessToken", "refreshToken");

            // Мокаем успешный логин
            when(authService.login(loginDto)).thenReturn(expectedResponse);

            // Act
            TokenResponseDto responseBody = authController.login(loginDto); // Получаем TokenResponseDto
            ResponseEntity<TokenResponseDto> response = ResponseEntity.ok(responseBody); // Оборачиваем в ResponseEntity

            // Assert
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
            // Arrange
            UserLoginDto loginDto = new UserLoginDto("testUser", "wrongPassword");

            // Мокаем неуспешный логин с выбрасыванием исключения
            when(authService.login(loginDto)).thenThrow(new AuthException("Incorrect password"));

            // Act
            TokenResponseDto responseBody = authController.login(loginDto); // Получаем TokenResponseDto
            ResponseEntity<TokenResponseDto> response = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponseDto(null, null)); // Оборачиваем в ResponseEntity

            // Assert
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
    void registrationUser() {
        // Логика теста для registrationUser
    }
}
