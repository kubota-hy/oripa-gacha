package com.example.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.GachaResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class GachaResultActionController {

    private final GachaResultService gachaResultService;

    @PostMapping("/{userId}/gacha-results/{resultId}/convert-coin")
    public void convertToCoin(
            @PathVariable Long userId,
            @PathVariable Long resultId) {

        gachaResultService.convertPendingToCoin(userId, resultId);
    }
}
