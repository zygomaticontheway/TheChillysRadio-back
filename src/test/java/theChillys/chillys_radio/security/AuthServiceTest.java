package theChillys.chillys_radio.security;

import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.service.IUserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private IUserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userService, tokenService, passwordEncoder);
    }


    @Test
    void login_SuccessfulLogin() throws AuthException {
        try {
            UserLoginDto inboundUser = new UserLoginDto("testUser", "password123");
            User foundUser = new User();
            foundUser.setName("testUser");
            foundUser.setPassword("encodedPassword");

            when(userService.loadUserByUsername("testUser")).thenReturn(foundUser);
            when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
            when(tokenService.generateAccessToken(foundUser)).thenReturn("accessToken");
            when(tokenService.generateRefreshToken(foundUser)).thenReturn("refreshToken");

            TokenResponseDto response = authService.login(inboundUser);

            assertNotNull(response);
            assertEquals("accessToken", response.getAccessToken());
            assertEquals("refreshToken", response.getRefreshToken());
            verify(userService).loadUserByUsername("testUser");
            verify(passwordEncoder).matches("password123", "encodedPassword");
            verify(tokenService).generateAccessToken(foundUser);
            verify(tokenService).generateRefreshToken(foundUser);

            System.out.println("Test login_SuccessfulLogin passed successfully!");
        } catch (AssertionError | Exception e) {
            System.out.println("Test login_SuccessfulLogin failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    void login_IncorrectPassword() {
        try {
            UserLoginDto inboundUser = new UserLoginDto("testUser", "wrongPassword");
            User foundUser = new User();
            foundUser.setName("testUser");
            foundUser.setPassword("encodedPassword");

            when(userService.loadUserByUsername("testUser")).thenReturn(foundUser);
            when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

            AuthException exception = assertThrows(AuthException.class, () -> authService.login(inboundUser));
            assertEquals("Incorrect password", exception.getMessage());
            verify(userService).loadUserByUsername("testUser");
            verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
            verify(tokenService, never()).generateAccessToken(any());
            verify(tokenService, never()).generateRefreshToken(any());

            System.out.println("Test login_IncorrectPassword passed successfully!");
        } catch (AssertionError | Exception e) {
            System.out.println("Test login_IncorrectPassword failed: " + e.getMessage());
            throw e;
        }
    }
}