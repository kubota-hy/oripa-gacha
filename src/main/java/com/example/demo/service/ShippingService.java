package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ShippingResponse;
import com.example.demo.repository.ShippingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final ShippingRepository shippingRepository;

    @Transactional(readOnly = true)
    public List<ShippingResponse> getUserShippingHistory(Long userId) {
        return shippingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(ShippingResponse::from)
                .toList();
    }
}
