package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.Shipping;

public record ShippingResponse(
        Long shippingId,
        Long gachaResultId,
        String status,
        String trackingNumber,
        LocalDateTime shippedAt,
        LocalDateTime createdAt
) {
    public static ShippingResponse from(Shipping s) {
        return new ShippingResponse(
                s.getId(),
                s.getGachaResultId(),
                s.getStatus().name(),
                s.getTrackingNumber(),
                s.getShippedAt(),
                s.getCreatedAt()
        );
    }
}
