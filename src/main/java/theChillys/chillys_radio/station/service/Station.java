package theChillys.chillys_radio.station.service;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import theChillys.chillys_radio.role.IRoleService;
import theChillys.chillys_radio.station.repository.IStationRepository;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.repository.IUserRepository;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor //делает конструктор только для final полей, для остальных не делает
@Service
public class Station implements IUserService { //можно также добавить в implements UserDetailsService, но мы уже добавили extends в IUserService

    private final IUserRepository repository;
    private final IRoleService roleService;
    private final BCryptPasswordEncoder encoder;
    private final ModelMapper mapper;


    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        if (dto == null || dto.getName() == null || dto.getUrl() == null){
            throw new IllegalArgumentException("Error: Name and URL are required");
        }
        User user = new User();

        User savedUser = repository.save(user);

        return (UserResponseDto) repository;
    }

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> users = repository.findById();

        // Преобразуем каждый объект User в объект UserResponseDto
        return users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList()); }


    @Override
    public Optional<UserResponseDto> getUserById(Long id) {

        return repository.findById(id)  // Возвращает Optional<User>
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ));
    }

    @Override
    public List<UserResponseDto> findUsersByNameOrEmail(String name, String email) {
        List<User> users = repository.findByNameContainingOrEmailContaining(name, email);
        return users.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUsersFavoriteStations(Long userId) {
      return null;
    }

    @Override
    public boolean setLike(Long userId, Long stationId) {
        return false;
    }

    @Override
    public boolean logOut(Long userId) {
        return repository.existsById(userId);
    }

    @Override
    public UserResponseDto setAdminRole(String username) {
        return null;
    }

    @Override
    public Optional<User> loadUserByUsername(String name) throws UsernameNotFoundException {

        Optional<User> user = null;
        if (user == null) {
            throw new UsernameNotFoundException("User is not found with username: " + username);
        }
        user = IUserRepository.findUserByName(username);
        return user;
    }
}
