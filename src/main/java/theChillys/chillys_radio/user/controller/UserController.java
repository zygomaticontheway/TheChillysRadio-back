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
@RequestMapping("/api")
public class UserController {


    @Autowired
    @Qualifier("userServiceImpl")
    private IUserService service;

    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return service.getUsers();
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }



    @GetMapping("/users/{userId}/favorites")
    public UserResponseDto getUsersFavoriteStations(@PathVariable Long userId) {
        return service.getUsersFavoriteStations(userId);
    }

    @PostMapping("/users/my-votes")
    public boolean setLike(@RequestBody String stationuuid,
                           @RequestBody String vote) {
        return service.setLike(stationuuid, vote);
    }


}

