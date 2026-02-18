package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gacha_items")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class GachaItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "gacha_id", nullable = false)
	private Gacha gacha;

	@ManyToOne
	@JoinColumn(name = "card_id", nullable = false)
	private Card card;

	@Column(name = "remaining_qty", nullable = false)
	private int remainingQty;
}
