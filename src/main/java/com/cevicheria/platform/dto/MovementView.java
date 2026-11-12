package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record MovementView(
        Long id,
        Long businessId,
        Long branchId,
        Long financialAccountId,
        Long cashShiftId,
        String type,
        BigDecimal amount,
        String concept,
        String operationKey
) {}
