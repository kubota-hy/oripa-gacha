package com.example.demo.dto;

import lombok.Data;

@Data
public class AdminStockAddRequest {

    private Long cardId;

    private Integer addQty;
}
