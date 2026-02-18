package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MyPageResponse(
        String email,
        Integer coin,
        List<ShippingHistoryDto> shippingHistory
) {
    public record ShippingHistoryDto(
            Long shippingId,
            Long gachaResultId,
            String cardName,
            String rarity,
            String shippingStatus,
            String trackingNumber,
            LocalDateTime shippedAt,
            LocalDateTime createdAt
    ) {}
}
