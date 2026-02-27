package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.GachaItemStockResponse;
import com.example.demo.entity.Card;
import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaItem;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.GachaItemRepository;
import com.example.demo.repository.GachaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminGachaStockService {

    private final GachaItemRepository gachaItemRepository;
    private final GachaRepository gachaRepository; // remaining_stock更新するなら
    private final CardRepository cardRepository;
    @Transactional
    public void addStock(Long gachaId, Long cardId, int addQty) {

        // ① 既存行を探す
        GachaItem item = gachaItemRepository
                .findByGachaIdAndCardId(gachaId, cardId)
                .orElse(null);

        if (item == null) {
            // ★ Entity を取得（ここが超重要）
            Gacha gacha = gachaRepository.findById(gachaId)
                    .orElseThrow(() -> new RuntimeException("gacha not found"));

            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new RuntimeException("card not found"));

            item = new GachaItem();
            item.setGacha(gacha);   // ← ここが正解
            item.setCard(card);     // ← ここが正解
            item.setRemainingQty(0);
        }

        // ② 数量加算
        item.setRemainingQty(item.getRemainingQty() + addQty);
        gachaItemRepository.save(item);

        // ③ ガチャ残数も増やす（あなたの方式なら）
        gachaRepository.incrementRemainingStock(gachaId, addQty);
    }
    public List<GachaItemStockResponse> listItems(Long gachaId) {
        return gachaItemRepository.findById(gachaId).stream()
            .map(gi -> new GachaItemStockResponse(
                gi.getId(),
                gi.getCard().getId(),
                gi.getCard().getCardName(),
                gi.getCard().getRarity() != null ? gi.getCard().getRarity().name() : null,
                gi.getRemainingQty()
            ))
            .toList();
    }
}
