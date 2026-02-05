package com.example.demo.dto;

import com.example.demo.entity.Gacha;

import lombok.Data;

@Data
public class GachaResponse {

    private Long id;
    private String name;
    private int priceCoin;
    private int remainingStock;

    public static GachaResponse from(Gacha gacha) {
        GachaResponse dto = new GachaResponse();
        dto.setId(gacha.getId());
        dto.setName(gacha.getName());
        dto.setPriceCoin(gacha.getPriceCoin());
        dto.setRemainingStock(gacha.getRemainingStock());
        return dto;
    }
}
