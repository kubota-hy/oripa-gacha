package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GachaItemStockResponse {
    private Long gachaItemId;
    private Long cardId;
    private String cardName;
    private String rarity;     // 取れれば
    private int remainingQty;
}
