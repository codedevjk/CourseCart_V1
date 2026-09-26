package com.coursecart.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.coursecart.user.dto.CountResponse;
import com.coursecart.user.dto.UserLoginRequest;
import com.coursecart.user.dto.UserRegistrationRequest;
import com.coursecart.user.dto.UserResponse;
import com.coursecart.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        throw new UnsupportedOperationException("TODO[TRAINEE]: Delegate to UserService to fetch user profile (US 03).");
    }

    @GetMapping("/count")
    public CountResponse countUsers() {
        return userService.countUsers();
    }
}
