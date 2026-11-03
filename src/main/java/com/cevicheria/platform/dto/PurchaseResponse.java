package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PurchaseStatus;
import com.cevicheria.platform.model.enums.PurchaseType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PurchaseResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long supplierId,
        Long purchaseOrderId,
        Long sourcePurchaseId,
        PurchaseType type,
        String series,
        String number,
        LocalDate recordDate,
        LocalDate expirationDate,
        String currencyCode,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,
        PurchaseStatus status,
        String notes,
        LocalDateTime createdAt) {
}
