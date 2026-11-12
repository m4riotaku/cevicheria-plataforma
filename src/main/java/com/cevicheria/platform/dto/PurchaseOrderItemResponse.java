package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PurchaseOrderItemResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long purchaseOrderId,
        Long ingredientId,
        Long presentationId,
        BigDecimal quantity,
        BigDecimal estimatedBaseQuantity,
        BigDecimal unitPrice,
        BigDecimal total,
        LocalDateTime createdAt) {
}
