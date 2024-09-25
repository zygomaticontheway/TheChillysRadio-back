package theChillys.chillys_radio.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @Operation(summary = "Find user", description = "Find a user by his username or password")
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

    @PostMapping("/users/my-favorites")
    public boolean toggleFavoriteStation(@RequestParam Long id, @RequestParam String stationuuid) {
        return service.toggleFavoriteStation(id, stationuuid);
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

    @GetMapping("/users/{userId}/favorites")
    public UserResponseDto getUsersFavoriteStations(@PathVariable Long userId) {
        return service.getUsersFavoriteStations(userId);
    }

    @GetMapping("/users/my-profile")
    public UserResponseDto getUserProfile(Principal principal) {
        String name = principal.getName();

        UserResponseDto userResponseDtoByName = service.getUserResponseDtoByName(name);
        Long id1 = userResponseDtoByName.getId();
        return getUsersFavoriteStations(id1);

    }

}



