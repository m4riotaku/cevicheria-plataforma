package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record SupplierIngredientRequest(
        Long businessId,
        Long supplierId,
        Long ingredientId,
        Long presentationId,
        String brand,
        BigDecimal referencePrice,
        Boolean preferred,
        Boolean active) {
}
