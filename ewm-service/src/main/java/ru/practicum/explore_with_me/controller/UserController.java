package ru.practicum.explore_with_me.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.UserDTOInput;
import ru.practicum.explore_with_me.dto.UserDtoOutputForAdmin;
import ru.practicum.explore_with_me.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    // These endpoints for only Admins
    @Autowired
    @Qualifier("UserServiceInBD")
    private final UserService userService;

    @GetMapping("/users")
    public List<UserDtoOutputForAdmin> get(@RequestParam(value = "ids", required = false)
                                               List<Long> ids,
                                           @RequestParam(value = "from", required = false, defaultValue = "0")
                                           Long from,
                                           @RequestParam(value = "size", required = false, defaultValue = "10")
                                               Long size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDtoOutputForAdmin create(@Valid @RequestBody UserDTOInput userDTOInput) {
        return userService.createUser(userDTOInput);
    }

    @DeleteMapping("/users/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
