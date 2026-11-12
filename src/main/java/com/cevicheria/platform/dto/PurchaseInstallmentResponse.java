package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PurchaseInstallmentResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long purchaseId,
        Integer number,
        LocalDate dueOn,
        BigDecimal amount,
        LocalDateTime createdAt) {
}
