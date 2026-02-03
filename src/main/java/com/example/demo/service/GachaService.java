package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Card;
import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.GachaRepository;
import com.example.demo.repository.GachaResultRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GachaService {

    private final UserRepository userRepository;
    private final GachaRepository gachaRepository;
    private final CardRepository cardRepository;
    private final GachaResultRepository gachaResultRepository;

    private final Random random = new Random();

    public GachaResult drawGacha(Long userId, Long gachaId) {

        // 1. ユーザー取得
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));

        // 2. ガチャ取得
        Gacha gacha = gachaRepository.findById(gachaId)
                .orElseThrow(() -> new RuntimeException("ガチャが存在しません"));

        // 3. 残り口数チェック
        if (gacha.getRemainingStock() <= 0) {
            throw new RuntimeException("このガチャはすでに終了しています");
        }

        // 4. コイン残高チェック
        if (user.getCoinBalance() < gacha.getPriceCoin()) {
            throw new RuntimeException("コインが不足しています");
        }

        // 5. コイン減算
        user.setCoinBalance(user.getCoinBalance() - gacha.getPriceCoin());
        userRepository.save(user);

        // 6. 残り口数減算
        gacha.setRemainingStock(gacha.getRemainingStock() - 1);
        gachaRepository.save(gacha);

        // 7. カード抽選
        List<Card> cards = cardRepository.findByGachaId(gachaId);
        if (cards.isEmpty()) {
            throw new RuntimeException("カードが設定されていません");
        }

        Card selectedCard = cards.get(random.nextInt(cards.size()));

        // 8. 結果保存
        GachaResult result = new GachaResult();
        result.setUser(user);
        result.setGacha(gacha);
        result.setCard(selectedCard);
        result.setResultType("CARD");
        result.setCreatedAt(LocalDateTime.now());

        return gachaResultRepository.save(result);
    }
    
    public Gacha findById(Long gachaId) {
        return gachaRepository.findById(gachaId)
                .orElseThrow(() -> new RuntimeException("ガチャが存在しません"));
    }
    
    public List<Gacha> getGachaList() {
        return gachaRepository.findAll();
    }
    
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));
    }



}
