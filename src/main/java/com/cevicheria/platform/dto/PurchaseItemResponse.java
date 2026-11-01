package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseItemResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long purchaseId,
        Long purchaseOrderItemId,
        Long ingredientId,
        Long presentationId,
        Long warehouseId,
        LocalDateTime receivedAt,
        BigDecimal presentationQuantity,
        BigDecimal acceptedBaseQuantity,
        BigDecimal rejectedBaseQuantity,
        BigDecimal grossWeight,
        BigDecimal tareWeight,
        BigDecimal netWeight,
        BigDecimal temperature,
        BigDecimal acceptedTotalCost,
        BigDecimal subtotal,
        BigDecimal tax,
        BigDecimal total,
        String rejectionReason,
        String evidenceUrl,
        LocalDateTime createdAt) {
}
