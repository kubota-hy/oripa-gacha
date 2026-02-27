package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.AdminUser;
import com.example.demo.service.AdminAuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public record AdminLoginRequest(String email, String password) {}
    public record AdminLoginResponse(Long adminId, String email, String role) {}

    @PostMapping("/login")
    public AdminLoginResponse login(@RequestBody AdminLoginRequest req) {
        AdminUser admin = adminAuthService.login(req.email(), req.password());

        return new AdminLoginResponse(
                admin.getId(),
                admin.getEmail(),
                admin.getRole()
        );
    }
}