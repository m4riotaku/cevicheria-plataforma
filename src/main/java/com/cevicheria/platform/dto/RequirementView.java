package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.SalesChannel;

public record RequirementView(
        Long businessId,
        Long commissionRuleId,
        boolean onlyVerifiedPaidSales,
        BigDecimal minimumSaleAmount,
        SalesChannel salesChannel
) {}
