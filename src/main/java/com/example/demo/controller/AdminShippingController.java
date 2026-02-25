package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Shipping;
import com.example.demo.service.AdminShippingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/shippings")
public class AdminShippingController {

    private final AdminShippingService adminShippingService;

    /**
     * 例: GET /admin/shippings?status=REQUESTED
     * MVPではREQUESTEDだけ返す運用でもOK
     */
    @GetMapping
    public List<Shipping> list(@RequestParam(required = false) String status) {
        return adminShippingService.list(status);
    }

    public record ShipRequest(String trackingNumber) {}

    /**
     * POST /admin/shippings/{id}/ship
     * body: { "trackingNumber": "xxxx" }
     */
    @PostMapping("/{id}/ship")
    public Shipping ship(@PathVariable Long id, @RequestBody ShipRequest req) {
        return adminShippingService.ship(id, req.trackingNumber());
    }

    /**
     * POST /admin/shippings/{id}/deliver
     */
    @PostMapping("/{id}/deliver")
    public Shipping deliver(@PathVariable Long id) {
        return adminShippingService.deliver(id);
    }
}
