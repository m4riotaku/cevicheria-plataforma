package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

import com.cevicheria.platform.model.enums.ProductType;

public record ProductResponse(
        Long id,
        Long businessId,
        String name,
        boolean active,
        Long categoryId,
        Long directIngredientId,
        String code,
        ProductType type,
        String saleUnit,
        String imageUrl,
        String taxCode,
        String description,
        LocalDateTime createdAt) {
}