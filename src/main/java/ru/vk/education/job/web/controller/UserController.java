package ru.vk.education.job.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.job.model.dto.UserCreateRequest;
import ru.vk.education.job.model.dto.UserResponse;
import ru.vk.education.job.model.entity.User;
import ru.vk.education.job.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<UserResponse>> getAllUsersSorted() {

        return ResponseEntity.ok(userService.getAllUsersSorted());
    }

    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> getUserByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserByName(name));
    }

    @PostMapping
    public void addUser(@RequestBody UserCreateRequest userCreateRequest) {
        userService.addUser(userCreateRequest);
    }
}