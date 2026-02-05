package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.example.demo.enums.Rarity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gacha_id", nullable = false)
    private Gacha gacha;

    @Column(name = "card_name", nullable = false)
    private String cardName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "coin_value", nullable = false)
    private Integer coinValue;
}

