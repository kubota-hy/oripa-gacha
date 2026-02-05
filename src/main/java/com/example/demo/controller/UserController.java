package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // GET /users/{userId}
    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return UserResponse.from(user);
    }

    // POST /users/{userId}/coins/add?amount=100
    @PostMapping("/{userId}/coins/add")
    public void addCoin(
            @PathVariable Long userId,
            @RequestParam int amount) {

        userService.addCoin(userId, amount);
    }
}
