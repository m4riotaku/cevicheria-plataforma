package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "combo_option")
public class ComboOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_product_id")
    private Product comboProduct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "option_product_id")
    private Product optionProduct;

    private Integer positionNumber;

    private String positionName;

    private BigDecimal fraction = BigDecimal.ONE;

    private BigDecimal surcharge = BigDecimal.ZERO;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}