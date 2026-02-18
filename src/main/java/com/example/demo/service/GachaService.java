package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Card;
import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaItem;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.User;
import com.example.demo.enums.GachaResultType;
import com.example.demo.repository.GachaItemRepository;
import com.example.demo.repository.GachaRepository;
import com.example.demo.repository.GachaResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GachaService {

    private final UserService userService;
    private final GachaRepository gachaRepository;
    private final GachaResultRepository gachaResultRepository;
    private final GachaItemRepository gachaItemRepository;

    @Transactional
    public GachaResult drawGacha(Long userId, Long gachaId) {

        Gacha gacha = gachaRepository.findById(gachaId)
                .orElseThrow(() -> new RuntimeException("ガチャが存在しません"));

        if (gacha.getRemainingStock() <= 0) {
            throw new RuntimeException("このガチャはすでに終了しています");
        }

        int price = gacha.getPriceCoin();
        userService.consumeCoin(userId, price);

        List<GachaItem> availableItems = gachaItemRepository.findAvailableForUpdate(gachaId);
        if (availableItems.isEmpty()) {
            throw new RuntimeException("封入在庫がありません");
        }

        int idx = ThreadLocalRandom.current().nextInt(availableItems.size());
        GachaItem selectedItem = availableItems.get(idx);
        Card selectedCard = selectedItem.getCard();

        selectedItem.setRemainingQty(selectedItem.getRemainingQty() - 1);
        gacha.setRemainingStock(gacha.getRemainingStock() - 1);

        User user = userService.findById(userId);

        GachaResult result = new GachaResult();
        result.setUser(user);
        result.setGacha(gacha);
        result.setCard(selectedCard);
        result.setCreatedAt(LocalDateTime.now());

        if (selectedCard.getRarity().isAutoConvert()) {
            userService.addCoin(userId, selectedCard.getCoinValue());
            result.setResultType(GachaResultType.AUTO_CONVERT);
        } else {
            result.setResultType(GachaResultType.PENDING);
        }

        return gachaResultRepository.save(result);
    }

    //ガチャ詳細
    @Transactional(readOnly = true)
    public Gacha findById(Long gachaId) {
        return gachaRepository.findById(gachaId)
                .orElseThrow(() -> new RuntimeException("ガチャが存在しません"));
    }

    //ガチャ一覧
    @Transactional(readOnly = true)
    public List<Gacha> getGachaList() {
        return gachaRepository.findAll();
    }
}
