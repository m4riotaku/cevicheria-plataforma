package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.SalesChannel;

public record RequirementRequest(
        Long businessId,
        Long commissionRuleId,
        Boolean onlyVerifiedPaidSales,
        BigDecimal minimumSaleAmount,
        SalesChannel salesChannel
) {}
