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
        // Инициализируем данные для тестов
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
        // Подготовка mock-объектов
        StationResponseDto stationResponseDto = new StationResponseDto();
        stationResponseDto.setStationuuid("stationUuid");
        stationResponseDto.setName("Test Station");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.map(station, StationResponseDto.class)).thenReturn(stationResponseDto);

        // Вызов тестируемого метода
        UserResponseDto responseDto = userServiceImpl.getUsersFavoriteStations(1L);

        // Проверка результатов
        try {
            assertNotNull(responseDto, "UserResponseDto не должен быть null");
            assertEquals(1L, responseDto.getId(), "ID пользователя должен быть 1L");
            assertEquals("Test User", responseDto.getName(), "Имя пользователя должно быть 'Test User'");
            assertEquals("test@example.com", responseDto.getEmail(), "Email пользователя должен быть 'test@example.com'");
            assertEquals(1, responseDto.getFavorites().size(), "Пользователь должен иметь 1 избранную станцию");
            assertEquals("stationUuid", responseDto.getFavorites().get(0).getStationuuid(), "UUID станции должен быть 'stationUuid'");
            assertEquals(1, responseDto.getRoles().size(), "Пользователь должен иметь 1 роль");
            assertTrue(responseDto.getRoles().stream().anyMatch(r -> r.getTitle().equals("ROLE_USER")), "Пользователь должен иметь роль 'ROLE_USER'");

            // Убедиться, что mock объекты были вызваны
            verify(userRepository).findById(1L);
            verify(mapper).map(station, StationResponseDto.class);

            System.out.println("Тест testGetUsersFavoriteStations успешно пройден!");
        } catch (AssertionError e) {
            System.out.println("Тест testGetUsersFavoriteStations не пройден: " + e.getMessage());
            throw e;
        }
    }
}
