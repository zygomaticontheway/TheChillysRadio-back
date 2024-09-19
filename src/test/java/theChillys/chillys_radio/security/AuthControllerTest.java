package theChillys.chillys_radio.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import theChillys.chillys_radio.user.service.UserServiceImpl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AuthControllerTest {

    private AuthController authController;


    private final AuthService authService = mock(AuthService.class);
    private final UserServiceImpl userService = mock(UserServiceImpl.class);

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService, userService);
    }

    @Test
    void login() {
        // Логика теста для login
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

            Mockito.verify(response).addCookie(Mockito.argThat(cookie ->
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
