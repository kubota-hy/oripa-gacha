package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MyPageResponse;
import com.example.demo.dto.MyPageResponse.ShippingHistoryDto;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.Shipping;
import com.example.demo.entity.User;
import com.example.demo.repository.GachaResultRepository;
import com.example.demo.repository.ShippingRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final ShippingRepository shippingRepository;
    private final GachaResultRepository gachaResultRepository;

    @Transactional(readOnly = true)
    public MyPageResponse getMyPage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Shipping> shippings = shippingRepository.findByUserIdOrderByCreatedAtDesc(userId);

        // gachaResultId をまとめる（nullガード）
        List<Long> resultIds = shippings.stream()
                .map(Shipping::getGachaResultId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 結果をまとめ取得（カード情報を触るならTransactional内でOK）
        Map<Long, GachaResult> resultMap = gachaResultRepository.findByIdIn(resultIds).stream()
                .collect(Collectors.toMap(GachaResult::getId, Function.identity()));

        List<ShippingHistoryDto> history = shippings.stream()
                .map(s -> toDto(s, resultMap.get(s.getGachaResultId())))
                .toList();

        return new MyPageResponse(
                user.getEmail(),
                user.getCoinBalance(),
                history
        );
    }

    private ShippingHistoryDto toDto(Shipping s, GachaResult r) {
        String cardName = null;
        String rarity = null;

        if (r != null && r.getCard() != null) {
            cardName = r.getCard().getCardName();
            rarity = r.getCard().getRarity().name();
        }

        return new ShippingHistoryDto(
                s.getId(),
                s.getGachaResultId(),
                cardName,
                rarity,
                s.getStatus().name(),
                s.getTrackingNumber(),
                s.getShippedAt(),
                s.getCreatedAt()
        );
    }
}
