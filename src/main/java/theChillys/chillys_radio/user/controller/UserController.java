package theChillys.chillys_radio.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import theChillys.chillys_radio.user.dto.UserRequestDto;
import theChillys.chillys_radio.user.dto.UserResponseDto;
import theChillys.chillys_radio.user.entity.User;
import theChillys.chillys_radio.user.service.IUserService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class UserController {
    @Qualifier("station")
    private final IUserService userService;

    @Qualifier("userServiceImpl")
    @Autowired
    private IUserService service;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        return service.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/search")
    public List<User> findUsersByNameOrEmail(@RequestParam(required = false) String name,
                                             @RequestParam(required = false) String email) {
        return userService.findUsersByNameOrEmail(name, email);
    }
}
