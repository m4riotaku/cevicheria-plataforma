package com.cevicheria.platform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.SalesChannel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Setter
@Entity
@Table(name = "branch_product")
public class BranchProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preparation_area_id")
    private PreparationArea preparationArea;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;

    private BigDecimal price = BigDecimal.ZERO;

    private BigDecimal taxRate = new BigDecimal("0.18");

    private String taxTreatmentCode = "10";

    private boolean visible = true;

    private boolean blocked = false;

    private String blockReason;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;
}