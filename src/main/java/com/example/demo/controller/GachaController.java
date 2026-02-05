package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.GachaDrawResponse;
import com.example.demo.dto.GachaResponse;
import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaResult;
import com.example.demo.service.GachaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gachas")
public class GachaController {

    private final GachaService gachaService;

    // GET /gachas
    @GetMapping
    public List<GachaResponse> getGachas() {
        List<Gacha> gachas = gachaService.getGachaList();
        return gachas.stream()
                .map(GachaResponse::from)
                .toList();
    }

    // GET /gachas/{gachaId}
    @GetMapping("/{gachaId}")
    public GachaResponse getGacha(@PathVariable Long gachaId) {
        return GachaResponse.from(gachaService.findById(gachaId));
    }

    // POST /gachas/{gachaId}/draw?userId=123
    @PostMapping("/{gachaId}/draw")
    public GachaDrawResponse draw(
            @PathVariable Long gachaId,
            @RequestParam Long userId) {

        GachaResult result = gachaService.drawGacha(userId, gachaId);
        return GachaDrawResponse.from(result);
    }
}
