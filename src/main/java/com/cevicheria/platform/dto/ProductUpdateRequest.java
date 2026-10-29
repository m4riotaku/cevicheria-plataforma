package com.cevicheria.platform.dto;

public record ProductUpdateRequest(
        String name,
        String code,
        Long categoryId,
        Long directIngredientId,
        String saleUnit,
        String imageUrl,
        String taxCode,
        String description,
        Boolean active) {
}