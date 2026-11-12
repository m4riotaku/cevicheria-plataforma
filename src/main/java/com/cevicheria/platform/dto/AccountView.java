package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.FinancialAccountType;

public record AccountView(
        Long id,
        Long businessId,
        Long branchId,
        String name,
        FinancialAccountType type,
        String currencyCode,
        String identifier,
        BigDecimal openingBalance,
        boolean active
) {}
