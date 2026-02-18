package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import com.example.demo.enums.CoinLedgerAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "coin_ledger",
    indexes = {
        @Index(name = "idx_coin_ledger_user_created_at", columnList = "user_id,created_at"),
        @Index(name = "idx_coin_ledger_admin_created_at", columnList = "admin_user_id,created_at")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "admin_user_id")
    private Long adminUserId; // 管理者認証が弱い間はnullでもOK

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CoinLedgerAction action;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "before_balance", nullable = false)
    private Integer beforeBalance;

    @Column(name = "after_balance", nullable = false)
    private Integer afterBalance;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
