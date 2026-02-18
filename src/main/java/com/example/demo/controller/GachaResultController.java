package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GachaResultResponse;
import com.example.demo.enums.GachaResultType;
import com.example.demo.service.GachaResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class GachaResultController {

    private final GachaResultService gachaResultService;

    // GET /users/{userId}/gacha-results
    // GET /users/{userId}/gacha-results?type=PENDING
    @GetMapping("/{userId}/gacha-results")
    public List<GachaResultResponse> getUserResults(
            @PathVariable Long userId,
            @RequestParam(required = false) GachaResultType type) {

        return gachaResultService.getUserResults(userId, type);
    }

    // GET /users/{userId}/gacha-results/pending
    @GetMapping("/{userId}/gacha-results/pending")
    public List<GachaResultResponse> getPendingResults(@PathVariable Long userId) {
        return gachaResultService.getPendingResults(userId);
    }

    // ==============================
    // 発送依頼
    // POST /users/{userId}/gacha-results/{resultId}/shipping-request
    // ==============================
    @PostMapping("/{userId}/gacha-results/{resultId}/shipping-request")
    public void requestShipping(
            @PathVariable Long userId,
            @PathVariable Long resultId) {

        gachaResultService.requestShipping(userId, resultId);
    }
}
