package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Card;
import com.example.demo.entity.Gacha;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.User;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.GachaRepository;
import com.example.demo.repository.GachaResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GachaService {

	private final UserService userService; // ★ UserRepositoryを直に持たない
	private final GachaRepository gachaRepository;
	private final CardRepository cardRepository;
	private final GachaResultRepository gachaResultRepository;

	private final Random random = new Random();

	@Transactional
	public GachaResult drawGacha(Long userId, Long gachaId) {

		// 1) ガチャ取得（本当はロック推奨：後述）
		Gacha gacha = gachaRepository.findById(gachaId)
				.orElseThrow(() -> new RuntimeException("ガチャが存在しません"));

		// 2) 在庫チェック（ガチャのルール）
		if (gacha.getRemainingStock() <= 0) {
			throw new RuntimeException("このガチャはすでに終了しています");
		}

		// 3) 価格決定（ガチャのルール）
		int price = gacha.getPriceCoin();

		// 4) 引き落とし（ユーザーのルール）※残高チェックもUserService側
		userService.consumeCoin(userId, price);

		// 5) 在庫減算（ガチャの状態変更）
		gacha.setRemainingStock(gacha.getRemainingStock() - 1);

		// 6) 抽選（ガチャのルール）
		List<Card> cards = cardRepository.findByGachaId(gachaId);
		if (cards.isEmpty()) {
			throw new RuntimeException("カードが設定されていません");
		}
		Card selectedCard = cards.get(random.nextInt(cards.size()));


		// 7) 結果作成・保存（取引の成果物）
		User user = userService.findById(userId); // 結果にUserを詰めるため
		GachaResult result = new GachaResult();
		result.setUser(user);
		result.setGacha(gacha);
		result.setCard(selectedCard);
		result.setResultType("CARD");
		result.setCreatedAt(LocalDateTime.now());


		if (selectedCard.getRarity().isAutoConvert()) {
			// 自動コイン変換
			userService.addCoin(userId, selectedCard.getCoinValue());
			result.setResultType("AUTO_CONVERT"); // or enum
		} else {
			// ユーザー選択待ち
			result.setResultType("PENDING");
		}

		return gachaResultRepository.save(result);
	}

	@Transactional(readOnly = true)
	public Gacha findById(Long gachaId) {
		return gachaRepository.findById(gachaId)
				.orElseThrow(() -> new RuntimeException("ガチャが存在しません"));
	}

	@Transactional(readOnly = true)
	public List<Gacha> getGachaList() {
		return gachaRepository.findAll();
	}


}
