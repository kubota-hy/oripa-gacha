package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.GachaResult;
import com.example.demo.enums.GachaResultType;

import lombok.Data;

@Data
public class GachaResultResponse {
    private Long resultId;
    private Long gachaId;
    private String gachaName;

    private Long cardId;
    private String cardName;
    private Integer cardCoinValue;
    private String rarity;

    private GachaResultType resultType;
    private LocalDateTime createdAt;

    public static GachaResultResponse from(GachaResult r) {
        GachaResultResponse dto = new GachaResultResponse();
        dto.setResultId(r.getId());
        dto.setGachaId(r.getGacha().getId());
        dto.setGachaName(r.getGacha().getName());
        dto.setCardId(r.getCard().getId());
        dto.setCardName(r.getCard().getCardName());
        dto.setCardCoinValue(r.getCard().getCoinValue());
        dto.setRarity(r.getCard().getRarity().name());
        dto.setResultType(r.getResultType());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }
}
