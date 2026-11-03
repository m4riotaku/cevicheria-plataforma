package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseItemRequest(
        Long businessId,
        Long branchId,
        Long purchaseId,
        Long ingredientId,
        Long warehouseId,
        LocalDateTime receivedAt,
        BigDecimal presentationQuantity,
        BigDecimal acceptedBaseQuantity,
        BigDecimal rejectedBaseQuantity,
        BigDecimal grossWeight,
        BigDecimal tareWeight,
        BigDecimal temperature,
        BigDecimal acceptedTotalCost,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,
        String rejectionReason,
        String evidenceUrl) {
}
