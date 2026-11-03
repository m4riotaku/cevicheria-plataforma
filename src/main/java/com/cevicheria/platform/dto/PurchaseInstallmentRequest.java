package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseInstallmentRequest(
        Long businessId,
        Long branchId,
        Long purchaseId,
        Integer number,
        LocalDate dueOn,
        BigDecimal amount) {
}
