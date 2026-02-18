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
import jakarta.persistence.UniqueConstraint;

import com.example.demo.enums.ShippingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "shipping",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_shipping_gacha_result_id", columnNames = "gacha_result_id")
    },
    indexes = {
        @Index(name = "idx_shipping_status_created_at", columnList = "status,created_at"),
        @Index(name = "idx_shipping_user_created_at", columnList = "user_id,created_at")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FKはDB側にあるが、Entityでは関連を張らずIDだけ持つ（あなたの方針A）
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "gacha_result_id", nullable = false)
    private Long gachaResultId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ShippingStatus status;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (status == null) status = ShippingStatus.REQUESTED;
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
