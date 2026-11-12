package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.cevicheria.platform.model.enums.CommissionRuleStates;

public record RuleView(
        Long id,
        Long businessId,
        String name,
        CommissionRuleStates type,
        BigDecimal valueAmount,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        boolean active
) {}
