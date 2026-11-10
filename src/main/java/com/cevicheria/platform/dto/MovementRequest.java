package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record MovementRequest(
        Long businessId,
        Long branchId,
        Long financialAccountId,
        Long cashShiftId,
        Long paymentId,
        Long responsibleEmployeeId,
        String type,
        BigDecimal amount,
        String concept,
        String operationKey
) {}
