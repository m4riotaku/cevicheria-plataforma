package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SupplierIngredientResponse(
        Long id,
        Long businessId,
        Long supplierId,
        Long ingredientId,
        Long presentationId,
        String brand,
        BigDecimal referencePrice,
        boolean preferred,
        boolean active,
        LocalDateTime createdAt) {
}
