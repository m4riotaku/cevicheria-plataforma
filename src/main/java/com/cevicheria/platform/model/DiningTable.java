package com.cevicheria.platform.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CurrentTimestamp;

import com.cevicheria.platform.model.enums.DiningTableStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "business_id",
        referencedColumnName = "id"
    )
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "branch_id",
        referencedColumnName = "id"
    )
    private Branch branch;


    @Column(nullable = false, length = 20)
    private String number;

    @Column(length = 80)
    private String zone;

    @Column(nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiningTableStatus status = DiningTableStatus.DISPONIBLE;

    @CurrentTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
