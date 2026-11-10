package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record ShiftRequest(
        Long businessId,
        Long branchId,
        Long cashRegisterId,
        Long responsibleEmployeeId,
        BigDecimal openingFund,
        String notes
) {}
