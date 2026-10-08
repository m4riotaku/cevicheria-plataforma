package com.cevicheria.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "delivery_zone", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"business_id", "branch_id", "name"})
})
public class DeliveryZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(nullable = false, length = 120)
    private String name; // Nombre de la zona (ej: "Miraflores", "San Isidro")

    @Column(name = "delivery_fee", nullable = false, precision = 18, scale = 2)
    private BigDecimal deliveryFee = BigDecimal.ZERO; // Tarifa de delivery para esta zona

    @Column(name = "estimated_minutes")
    private Integer estimatedMinutes; // Tiempo estimado de entrega en minutos

    @Column(nullable = false)
    private boolean active = true;
}
