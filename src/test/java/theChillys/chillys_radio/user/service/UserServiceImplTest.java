package theChillys.chillys_radio.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IUserRepository;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleService roleService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private IStationRepository stationRepository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private Station station;
    private Role role;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        station = new Station();
        station.setStationuuid("stationUuid");
        station.setName("Test Station");

        role = new Role();
        role.setTitle("ROLE_USER");

        Set<Station> favoriteStations = new HashSet<>();
        favoriteStations.add(station);
        user.setFavorites(favoriteStations);

        user.setRoles(Set.of(role));
    }


    @Test
    void createUser_SuccessfulCreation() {
        try {
            UserRequestDto userRequestDto = new UserRequestDto();
            userRequestDto.setName("New User");
            userRequestDto.setEmail("newuser@example.com");
            userRequestDto.setPassword("password123");

            User newUser = new User();
            newUser.setName("New User");
            newUser.setEmail("newuser@example.com");
            newUser.setPassword("encodedPassword");
            newUser.setRoles(Set.of(role));
            newUser.setFavorites(new HashSet<>());

            when(userRepository.findUserByName("New User")).thenReturn(Optional.empty());
            when(roleService.getRoleByTitle("ROLE_USER")).thenReturn(role);
            when(encoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(newUser);
            when(mapper.map(newUser, UserResponseDto.class)).thenReturn(new UserResponseDto());

            UserResponseDto response = userServiceImpl.createUser(userRequestDto);

            assertNotNull(response, "Response should not be null");
            verify(userRepository).findUserByName("New User");
            verify(roleService).getRoleByTitle("ROLE_USER");
            verify(encoder).encode("password123");
            verify(userRepository).save(any(User.class));
            verify(mapper).map(newUser, UserResponseDto.class);

            System.out.println("Test createUser_SuccessfulCreation passed successfully!");
        } catch (AssertionError e) {
            System.out.println("Test createUser_SuccessfulCreation failed: " + e.getMessage());
            fail("Test createUser_SuccessfulCreation failed");
        }
    }

    @Test
    void createUser_UserAlreadyExists() {
        try {

            UserRequestDto userRequestDto = new UserRequestDto();
            userRequestDto.setName("Existing User");
            userRequestDto.setEmail("existinguser@example.com");
            userRequestDto.setPassword("password123");

            when(userRepository.findUserByName("Existing User")).thenReturn(Optional.of(user));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                userServiceImpl.createUser(userRequestDto);
            });

            assertEquals("User Existing User already exists", exception.getMessage());
            verify(userRepository).findUserByName("Existing User");

            System.out.println("Test createUser_UserAlreadyExists passed successfully!" );
        } catch (AssertionError e) {
            System.out.println("Test createUser_UserAlreadyExists failed: " + e.getMessage());
            fail("Test createUser_UserAlreadyExists failed");
        }
    }

    @Test
    void createUser_MissingRequiredFields() {
        try {
            UserRequestDto userRequestDto = new UserRequestDto();
            userRequestDto.setName("");
            userRequestDto.setEmail("");
            userRequestDto.setPassword("");

            // Act & Assert
            IllegalArgumentException exceptionName = assertThrows(IllegalArgumentException.class, () -> {
                userServiceImpl.createUser(userRequestDto);
            });
            assertEquals("User name is required", exceptionName.getMessage());

            userRequestDto.setName("New User");
            IllegalArgumentException exceptionEmail = assertThrows(IllegalArgumentException.class, () -> {
                userServiceImpl.createUser(userRequestDto);
            });
            assertEquals("Email is required", exceptionEmail.getMessage());

            userRequestDto.setEmail("newuser@example.com");
            IllegalArgumentException exceptionPassword = assertThrows(IllegalArgumentException.class, () -> {
                userServiceImpl.createUser(userRequestDto);
            });
            assertEquals("Password is required", exceptionPassword.getMessage());

            System.out.println("Test createUser_MissingRequiredFields passed successfully!");
        } catch (AssertionError e) {
            System.out.println("Test createUser_MissingRequiredFields failed: " + e.getMessage());
            fail("Test createUser_MissingRequiredFields failed");
        }
    }



    @Test
    void testGetUsersFavoriteStations() {

        StationResponseDto stationResponseDto = new StationResponseDto();
        stationResponseDto.setStationuuid("stationUuid");
        stationResponseDto.setName("Test Station");

        when(userRepository.findUserByName("admin")).thenReturn(Optional.of(user));
        when(mapper.map(station, StationResponseDto.class)).thenReturn(stationResponseDto);

        List<StationResponseDto> responseDto = userServiceImpl.getUsersFavoriteStations("admin");

        try {
            assertNotNull(responseDto, "UserResponseDto must not be null");
            //TODO Не обязательно у пользователя станции в избранном быть должны. Чаще всего их там не будет. Нужно переписать тест
            assertEquals(1, responseDto.size(), "User must have 1 favorite station");

            verify(userRepository).findById(1L);
            verify(mapper).map(station, StationResponseDto.class);

            System.out.println("Test testGetUsersFavoriteStations passed successfully!");
        } catch (AssertionError e) {
            System.out.println("Test testGetUsersFavoriteStations failed: " + e.getMessage());
            throw e;
        }
    }

}
