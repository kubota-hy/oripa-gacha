package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Shipping;
import com.example.demo.enums.ShippingStatus;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    // ユーザーの発送履歴
    List<Shipping> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 管理者：ステータス別一覧
    List<Shipping> findByStatusOrderByCreatedAtDesc(ShippingStatus status);

    // 管理者：全件
    List<Shipping> findAllByOrderByCreatedAtDesc();

    // 発送依頼の重複防止
    boolean existsByGachaResultId(Long gachaResultId);
}