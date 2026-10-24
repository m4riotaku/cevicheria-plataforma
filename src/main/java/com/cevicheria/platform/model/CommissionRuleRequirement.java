package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.enums.SalesChannel;
import com.cevicheria.platform.model.id.CommissionRuleRequirementId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommissionRuleRequirement {

    @EmbeddedId
    private CommissionRuleRequirementId id;

    private boolean onlyVerifiedPaidSales = false;

    private BigDecimal minimumSaleAmount;

    @Enumerated(EnumType.STRING)
    private SalesChannel salesChannel;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("commissionRuleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commission_rule_id")
    private CommissionRule commissionRule;

    public CommissionRuleRequirement(
            Business business,
            CommissionRule commissionRule,
            BigDecimal minimumSaleAmount,
            SalesChannel salesChannel) {
        this.id = new CommissionRuleRequirementId(null, null);
        changeBusiness(business);
        changeCommissionRule(commissionRule);
        changeMinimumSaleAmount(minimumSaleAmount);
        changeSalesChannel(salesChannel);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new CommissionRuleRequirementId(business.getId(), this.id.getCommissionRuleId());
    }

    private void changeCommissionRule(CommissionRule commissionRule) {
        if (commissionRule == null) {
            throw new InvalidEntityDataException("La regla de comisión es obligatoria");
        }
        if (commissionRule.getId() == null) {
            throw new InvalidEntityDataException("La regla de comisión debe estar registrada antes de realizar la asociación");
        }
        this.commissionRule = commissionRule;
        this.id = new CommissionRuleRequirementId(this.id.getBusinessId(), commissionRule.getId());
    }

    public void changeMinimumSaleAmount(BigDecimal minimumSaleAmount) {
        if (minimumSaleAmount == null) {
            this.minimumSaleAmount = null;
            return;
        }
        if (minimumSaleAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidEntityDataException("El importe mínimo de la venta debe ser cero o positivo");
        }
        if (minimumSaleAmount.abs().compareTo(new BigDecimal("9999999999999999.99")) > 0) {
            throw new InvalidEntityDataException("El importe mínimo de la venta excede DECIMAL(18,2)");
        }
        if (minimumSaleAmount.stripTrailingZeros().scale() > 2) {
            throw new InvalidEntityDataException("El importe mínimo de la venta admite hasta 2 decimales");
        }
        this.minimumSaleAmount = minimumSaleAmount.setScale(2);
    }

    public void changeSalesChannel(SalesChannel salesChannel) {
        this.salesChannel = salesChannel;
    }

    public void requireVerifiedPaidSales() {
        this.onlyVerifiedPaidSales = true;
    }

    public void allowUnverifiedPaidSales() {
        this.onlyVerifiedPaidSales = false;
    }
}
