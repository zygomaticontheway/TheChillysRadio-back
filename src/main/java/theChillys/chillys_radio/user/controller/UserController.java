package theChillys.chillys_radio.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public UserController(IUserService service) {
        this.service = service;
    }

    @Autowired
    @Qualifier("userServiceImpl")
    private  IUserService service;

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping("/{userId}/favorites")
    public UserResponseDto getUsersFavoriteStations(@PathVariable Long userId) {
        return service.getUsersFavoriteStations(userId);
    }
    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return IUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return IUserService.getUserById(id);
    }

    @GetMapping("/search")
    public List<UserResponseDto> findUsersByNameOrEmail(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String email) {
        return IUserService.findUsersByNameOrEmail(name, email);
    }
}

