package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.AdminUser;
import com.example.demo.repository.AdminUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserRepository adminUserRepository;

    public AdminUser login(String email, String rawPassword) {
        AdminUser admin = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("admin not found"));

        // ★ ここ重要：今の実装に合わせる
        // BCrypt使ってない前提（最短）
        if (!admin.getPasswordHash().equals(rawPassword)) {
            throw new RuntimeException("invalid password");
        }

        return admin;
    }
}
