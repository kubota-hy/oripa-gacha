package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ShippingResponse;
import com.example.demo.service.ShippingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class ShippingController {

    private final ShippingService shippingService;

    // GET /users/{userId}/shippings
    @GetMapping("/{userId}/shippings")
    public List<ShippingResponse> getUserShippings(@PathVariable Long userId) {
        return shippingService.getUserShippingHistory(userId);
    }
}
