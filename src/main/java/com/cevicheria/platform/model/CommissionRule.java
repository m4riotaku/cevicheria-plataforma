package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.CommissionRuleStates;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    private String name;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Enumerated(EnumType.STRING)
    private CommissionRuleStates type;

    private BigDecimal valueAmount = new BigDecimal("0.00");

    private LocalDateTime validFrom;

    private LocalDateTime validUntil;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    public CommissionRule(Business business, String name, CommissionRuleStates type, LocalDateTime validFrom) {
        changeBusiness(business);
        changeName(name);
        changeType(type);
        changeValidFrom(validFrom);
    }

    private void changeBusiness(Business value) {
        if (value == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = value;
    }

    public void changeName(String value) {
        if (value == null) {
            throw new InvalidEntityDataException("El nombre de la regla de comisión es obligatorio");
        }
        if (value.isBlank()) {
            throw new InvalidEntityDataException("El nombre del regla de comisión no puede estar vacío");
        }
        String normalized = value.trim();
        if (normalized.length() > 120) {
            throw new InvalidEntityDataException("El nombre del regla de comisión no puede superar 120 caracteres");
        }
        this.name = normalized;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    private void changeProduct(Product value) {
        this.product = value;
    }

    public void changeCategory(Category value) {
        this.category = value;
    }

    private void changeEmployee(Employee value) {
        this.employee = value;
    }

    public void changeType(CommissionRuleStates value) {
        if (value == null) {
            throw new InvalidEntityDataException("El tipo es obligatorio");
        }
        this.type = value;
    }

    public void changeValueAmount(BigDecimal value) {
        if (value != null && value.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El value importe supera el máximo permitido");
        }
        if (value != null && value.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El value importe no admite más de 2 decimales");
        }
        if (value != null && value.signum() < 0) {
            throw new InvalidEntityDataException("El value importe no puede ser negativo");
        }
        this.valueAmount = value;
    }

    private void changeValidFrom(LocalDateTime value) {
        if (value == null) {
            throw new InvalidEntityDataException("El inicio de vigencia es obligatoria");
        }
        this.validFrom = value;
    }

    public void changeValidUntil(LocalDateTime value) {
        this.validUntil = value;
    }


    @Getter(AccessLevel.NONE)
    @OneToMany(mappedBy = "commissionRule", fetch = FetchType.LAZY)
    private List<CommissionRuleRequirement> requirements = new ArrayList<>();

    public List<CommissionRuleRequirement> getRequirements() {
        return List.copyOf(this.requirements);
    }
}