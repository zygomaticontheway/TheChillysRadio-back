package theChillys.chillys_radio.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import theChillys.chillys_radio.role.Role;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.station.entity.Station;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IUserRepository;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.station.repository.IStationRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
    void testGetUsersFavoriteStations() {

        StationResponseDto stationResponseDto = new StationResponseDto();
        stationResponseDto.setStationuuid("stationUuid");
        stationResponseDto.setName("Test Station");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.map(station, StationResponseDto.class)).thenReturn(stationResponseDto);

        UserResponseDto responseDto = userServiceImpl.getUsersFavoriteStations(1L);

        try {
            assertNotNull(responseDto, "UserResponseDto must not be null");
            assertEquals(1L, responseDto.getId(), "User ID must be 1L");
            assertEquals("Test User", responseDto.getName(), "Username must be 'Test User'");
            assertEquals("test@example.com", responseDto.getEmail(), "User email must be 'test@example.com'");
            assertEquals(1, responseDto.getFavorites().size(), "User must have 1 favorite station");
            assertEquals("stationUuid", responseDto.getFavorites().get(0).getStationuuid(), "Station UUID must be 'stationUuid'");
            assertEquals(1, responseDto.getRoles().size(), "User must have 1 role");
            assertTrue(responseDto.getRoles().stream().anyMatch(r -> r.getTitle().equals("ROLE_USER")), "User must have role 'ROLE_USER'");

            verify(userRepository).findById(1L);
            verify(mapper).map(station, StationResponseDto.class);

            System.out.println("Test testGetUsersFavoriteStations passed successfully!");
        } catch (AssertionError e) {
            System.out.println("Test testGetUsersFavoriteStations failed: " + e.getMessage());
            throw e;
        }
    }
}
