package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record PurchaseOrderItemRequest(
        Long businessId,
        Long branchId,
        Long purchaseOrderId,
        Long ingredientId,
        Long presentationId,
        BigDecimal quantity,
        BigDecimal estimatedBaseQuantity,
        BigDecimal unitPrice,
        BigDecimal total) {
}
