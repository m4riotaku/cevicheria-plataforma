package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersRecipeSnapshotView(Long orderItemId, Integer recipeVersion, BigDecimal servings, BigDecimal yieldRatio, String instructions) {}
