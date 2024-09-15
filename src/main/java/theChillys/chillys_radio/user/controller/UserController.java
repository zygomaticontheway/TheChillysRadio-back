package theChillys.chillys_radio.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService service;

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping("/{userId}/favorites")
    public UserResponseDto getUsersFavoriteStations(@PathVariable Long userId) {
        return service.getUsersFavoriteStations(userId);
    }

    @PostMapping("/{userId}/my-votes/{stationId}")
    public boolean setLike(
            @PathVariable Long userId,
            @PathVariable Long stationId) {
        return service.setLike(userId, stationId);
    }


    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return service.getUsers();
}

     @GetMapping("/users/{id}")
     public Optional<UserResponseDto> getUserById(@PathVariable(name="id") Long userId) {
        return service.getUserById(Long.valueOf(String.valueOf(id)));
}

     @GetMapping("/users?name=...&email=...")
     public List<UserResponseDto> findUsersByNameOrEmail(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String email) {
         return service.findUsersByNameOrEmail(name, email);
     }
     @PostMapping("/users/my-favorites")
     public boolean toggleFavoriteStation(@RequestParam Long userId, @RequestParam String stationUuid) {
         return service.toggleFavoriteStation(userId, stationUuid);
     }

}



