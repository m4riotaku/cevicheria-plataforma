package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record ApplicationRequest(
        Long businessId,
        Long branchId,
        Long paymentId,
        Long orderBillId,
        Long purchaseInstallmentId,
        Long reservationId,
        BigDecimal amount,
        String applicationKey
) {}
