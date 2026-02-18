package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AdminUserService;
import com.example.demo.service.AdminUserService.CoinAdjustResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public record CoinAdjustRequest(Integer amount, String reason, Long adminUserId) {}
    public record CoinSetRequest(Integer newBalance, String reason, Long adminUserId) {}

    @PostMapping("/{userId}/coins/grant")
    public CoinAdjustResult grant(@PathVariable Long userId, @RequestBody CoinAdjustRequest req) {
        return adminUserService.grantCoin(userId, req.amount(), req.reason(), req.adminUserId());
    }

    @PostMapping("/{userId}/coins/deduct")
    public CoinAdjustResult deduct(@PathVariable Long userId, @RequestBody CoinAdjustRequest req) {
        return adminUserService.deductCoin(userId, req.amount(), req.reason(), req.adminUserId());
    }

    @PostMapping("/{userId}/coins/set")
    public CoinAdjustResult set(@PathVariable Long userId, @RequestBody CoinSetRequest req) {
        return adminUserService.setCoin(userId, req.newBalance(), req.reason(), req.adminUserId());
    }
}
