package com.cevicheria.platform.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "combo_option")
public class ComboOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long comboProductId;

    @Column(nullable = false)
    private Long optionProductId;

    @Column(nullable = false)
    private Integer minimumQuantity = 1;

    @Column(nullable = false)
    private Integer maximumQuantity = 1;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal additionalPrice = BigDecimal.ZERO;
}