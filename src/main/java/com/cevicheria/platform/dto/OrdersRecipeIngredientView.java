package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersRecipeIngredientView(Long orderItemId, Long ingredientId, String name, String unit, BigDecimal baseQuantityPerServing) {}
