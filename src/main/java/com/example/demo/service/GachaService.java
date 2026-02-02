package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
public class GachaService {
	
	private final UserRepository userRepository;
	private final GachaRepository gachaRepository;
	private final CardRepository cardRepository;
	private final GachaResultRepository gachaResultRepository;

	public GachaResult drawGacha(Long userId, Long gachaId) {

	    // 1. ユーザー取得
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));

	    // 2. ガチャ取得
	    Gacha gacha = gachaRepository.findById(gachaId)
	    		.orElseThrow(() -> new RuntimeException("ガチャが存在しません"));

	    // 3. コイン残高チェック
	    if (user.getCoinBalance() < gacha.getPriceCoin()) {
	        throw new RuntimeException("コインが不足しています");
	    }

	    // 4. コイン減算
	    user.setCoinBalance(user.getCoinBalance() - gacha.getPriceCoin());
	    userRepository.save(user);

	    // 5. カード一覧取得
	    List<Card> cards = cardRepository.findByGachaId(gachaId);
	    if (cards.isEmpty()) {
	        throw new RuntimeException("カードが設定されていません");
	    }

	    // 6. ランダムで1枚選択
	    Card selectedCard = cards.get(new Random().nextInt(cards.size()));

	    // 7. 結果保存
	    GachaResult result = new GachaResult();
	    result.setUser(user);
	    result.setGacha(gacha);
	    result.setCard(selectedCard);
	    result.setResultType("CARD");
	    result.setCreatedAt(LocalDateTime.now());

	    return gachaResultRepository.save(result);
	}

}

