package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.GachaResult;

import lombok.Data;

@Data
public class GachaDrawResponse {
    private Long resultId;
    private Long gachaId;
    private Long userId;

    private Long cardId;
    private String cardName;

    private Integer cardCoinValue; // ★追加（CardのcoinValueを見せる）

    private LocalDateTime createdAt;

    public static GachaDrawResponse from(GachaResult result) {
        GachaDrawResponse dto = new GachaDrawResponse();
        dto.setResultId(result.getId());
        dto.setGachaId(result.getGacha().getId());
        dto.setUserId(result.getUser().getId());

        dto.setCardId(result.getCard().getId());
        dto.setCardName(result.getCard().getCardName());

        dto.setCardCoinValue(result.getCard().getCoinValue()); // ★ここ

        dto.setCreatedAt(result.getCreatedAt());
        return dto;
    }
}

