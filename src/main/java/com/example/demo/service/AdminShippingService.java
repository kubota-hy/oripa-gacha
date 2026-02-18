package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Shipping;
import com.example.demo.enums.ShippingStatus;
import com.example.demo.repository.ShippingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminShippingService {

    private final ShippingRepository shippingRepository;

    /**
     * 発送待ち（REQUESTED）一覧
     */
    @Transactional(readOnly = true)
    public List<Shipping> getRequestedList() {
        return shippingRepository.findByStatusOrderByCreatedAtDesc(ShippingStatus.REQUESTED);
    }

    /**
     * 発送確定：REQUESTED -> SHIPPED
     * trackingNumber必須（運用上の事故防止）
     */
    @Transactional
    public Shipping ship(Long shippingId, String trackingNumber) {
        if (trackingNumber == null || trackingNumber.isBlank()) {
            throw new IllegalArgumentException("trackingNumber is required");
        }

        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new IllegalArgumentException("Shipping not found: " + shippingId));

        if (shipping.getStatus() != ShippingStatus.REQUESTED) {
            throw new IllegalStateException("Only REQUESTED can be shipped. current=" + shipping.getStatus());
        }

        shipping.setTrackingNumber(trackingNumber.trim());
        shipping.setShippedAt(LocalDateTime.now());
        shipping.setStatus(ShippingStatus.SHIPPED);

        return shippingRepository.save(shipping);
    }

    /**
     * 配達完了：SHIPPED -> DELIVERED
     */
    @Transactional
    public Shipping deliver(Long shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new IllegalArgumentException("Shipping not found: " + shippingId));

        if (shipping.getStatus() != ShippingStatus.SHIPPED) {
            throw new IllegalStateException("Only SHIPPED can be delivered. current=" + shipping.getStatus());
        }

        shipping.setStatus(ShippingStatus.DELIVERED);
        return shippingRepository.save(shipping);
    }
}
