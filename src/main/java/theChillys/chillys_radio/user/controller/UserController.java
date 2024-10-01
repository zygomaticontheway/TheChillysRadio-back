package theChillys.chillys_radio.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.station.dto.StationResponseDto;
import theChillys.chillys_radio.user.dto.ChangePasswordDto;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.IUserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {


    @Autowired
    @Qualifier("userServiceImpl")
    private final IUserService service;


    @GetMapping("/users")
    public List<UserResponseDto> findUsersByNameOrEmail(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String email) {
        if ((name == null || name.isEmpty()) && (email == null || email.isEmpty())) {
            return service.getUsers();
        } else {
            return service.findUsersByNameOrEmail(name, email);
        }
    }

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping("/users/{id}")
    public Optional<UserResponseDto> getUserById(@PathVariable(name = "id") Long userId) {
        return service.getUserById(userId);
    }

    @GetMapping("/users/my-favorites")
    public List<StationResponseDto> getUsersFavoriteStations( Principal principal) {

        String name = principal.getName();

        return service.getUsersFavoriteStations(name);
    }

    @PostMapping("/users/my-favorites")
    public List<StationResponseDto> toggleFavoriteStation(@RequestBody String stationuuid, Principal principal) {

        String name = principal.getName();

        return service.toggleFavoriteStation(name, stationuuid);
    }

    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable(name = "id") Long Id, @RequestBody UserRequestDto dto) {
        return service.updateUser(Id, dto);
    }

    @PostMapping("/users/{id}/change-password")
    public UserResponseDto changePassword(@PathVariable(name = "id") Long userId, @RequestBody ChangePasswordDto passwordDto) {
        return service.changePassword(userId, passwordDto.getNewPassword());

    }

    @PreAuthorize("hasRole('ADMIN')")  // only for admin
    @PutMapping("/set-admin/{name}")
    public UserResponseDto setAdminRole(@PathVariable(name = "name") String name) {
        return service.setAdminRole(name);
    }

    @GetMapping("/users/my-profile")
    public UserResponseDto getUserProfile(Principal principal) {
        String name = principal.getName();

        return service.getUserResponseDtoByName(name);
    }

}



