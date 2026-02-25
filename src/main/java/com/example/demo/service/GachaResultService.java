package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.GachaResultResponse;
import com.example.demo.entity.GachaResult;
import com.example.demo.entity.Shipping;
import com.example.demo.enums.GachaResultType;
import com.example.demo.enums.ShippingStatus;
import com.example.demo.repository.GachaResultRepository;
import com.example.demo.repository.ShippingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GachaResultService {

    private final UserService userService;
    private final GachaResultRepository gachaResultRepository;
    private final ShippingRepository shippingRepository;

    @Transactional(readOnly = true)
    public List<GachaResultResponse> getUserResults(Long userId, GachaResultType type) {
        if (type == null) {
            return gachaResultRepository.findByUserIdOrderByCreatedAtDesc(userId)
                    .stream().map(GachaResultResponse::from).toList();
        }
        return gachaResultRepository.findByUserIdAndResultTypeOrderByCreatedAtDesc(userId, type)
                .stream().map(GachaResultResponse::from).toList();
    }

    @Transactional
    public void convertPendingToCoin(Long userId, Long resultId) {

        GachaResult result = gachaResultRepository.findByIdForUpdate(resultId)
                .orElseThrow(() -> new RuntimeException("当選履歴が存在しません"));

        if (!result.getUser().getId().equals(userId)) {
            throw new RuntimeException("他ユーザーの当選履歴は操作できません");
        }

        if (result.getResultType() != GachaResultType.PENDING) {
            throw new RuntimeException("この当選履歴はコイン変換できません");
        }

        int value = result.getCard().getCoinValue();
        userService.addCoin(userId, value);

        result.setResultType(GachaResultType.COIN_CONVERTED);
    }

    @Transactional(readOnly = true)
    public List<GachaResultResponse> getPendingResults(Long userId) {

        // ① PENDING取得
        List<GachaResult> pendings = gachaResultRepository
                .findByUserIdAndResultTypeOrderByCreatedAtDesc(userId, GachaResultType.PENDING);

        // ② そのユーザーのShipping（発送依頼済み/発送済み/配達済み全部）を取得してSet化
        java.util.Set<Long> shippedOrRequestedResultIds = shippingRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(s -> s.getGachaResultId())
                .collect(java.util.stream.Collectors.toSet());

        // ③ Shippingが存在するものを除外
        return pendings.stream()
                .filter(r -> !shippedOrRequestedResultIds.contains(r.getId()))
                .map(GachaResultResponse::from)
                .toList();
    }


    @Transactional
    public void requestShipping(Long userId, Long resultId) {

        GachaResult result = gachaResultRepository.findByIdForUpdate(resultId)
                .orElseThrow(() -> new RuntimeException("当選履歴が存在しません"));

        // セキュリティ：他人の当選履歴は操作不可
        if (!result.getUser().getId().equals(userId)) {
            throw new RuntimeException("他ユーザーの当選履歴は操作できません");
        }

        // PENDING以外は発送依頼不可（2回目防止）
        if (result.getResultType() != GachaResultType.PENDING) {
            throw new RuntimeException("この当選履歴は発送依頼できません");
        }

        // 既に発送依頼済み（shippingが存在）ならNG
        if (shippingRepository.existsByGachaResultId(resultId)) {
            throw new RuntimeException("この当選履歴は既に発送依頼済みです");
        }

        Shipping shipping = Shipping.builder()
                .userId(userId)
                .gachaResultId(resultId)
                .status(ShippingStatus.REQUESTED)
                .trackingNumber(null)
                .shippedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        shippingRepository.save(shipping);

        // GachaResultTypeにSHIPPING系を増やさない方針なので、resultTypeは変更しない
        // 「発送したかどうか」は shipping の有無で判定する
    }
}
