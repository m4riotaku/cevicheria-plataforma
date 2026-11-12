package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.ProductType;

public record ProductCreateRequest(
        Long businessId,
        String name,
        String code,
        ProductType type,
        Long categoryId,
        Long directIngredientId,
        String saleUnit,
        String imageUrl,
        String taxCode,
        String description) {
}