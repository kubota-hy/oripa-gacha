package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AdminStockAddRequest;
import com.example.demo.dto.GachaItemStockResponse;
import com.example.demo.service.AdminGachaStockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/gachas")
public class AdminGachaStockController {

    private final AdminGachaStockService adminGachaStockService;

    // 在庫一覧（管理者画面で表示用）
    // GET /admin/gachas/{gachaId}/items
    @GetMapping("/{gachaId}/items")
    public List<GachaItemStockResponse> listItems(@PathVariable Long gachaId) {
        return adminGachaStockService.listItems(gachaId);
    }

    // 在庫追加（補充）
    // POST /admin/gachas/{gachaId}/items/stock
    @PostMapping("/{gachaId}/items/stock")
    public ResponseEntity<Void> addStock(
            @PathVariable Long gachaId,
            @RequestBody AdminStockAddRequest req
    ) {
        adminGachaStockService.addStock(gachaId, req.getCardId(), req.getAddQty());
        return ResponseEntity.noContent().build();
    }
}