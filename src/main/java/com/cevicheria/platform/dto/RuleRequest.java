package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.cevicheria.platform.model.enums.CommissionRuleStates;

public record RuleRequest(
        Long businessId,
        String name,
        CommissionRuleStates type,
        BigDecimal valueAmount,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        Boolean active
) {}
