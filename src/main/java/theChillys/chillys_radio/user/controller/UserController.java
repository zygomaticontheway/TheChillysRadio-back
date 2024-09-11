package theChillys.chillys_radio.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.user.dto.ChangePasswordDto;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.IUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
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

    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable(name = "id") Long Id, @RequestBody UserRequestDto dto) {
        return service.updateUser(Id, dto);
    }

    @PostMapping("/users/{id}/change-password")
    public UserResponseDto changePassword(@PathVariable(name = "id") Long userId, @RequestBody ChangePasswordDto passwordDto) {
        return service.changePassword(userId, passwordDto.getNewPassword());

    }
}

