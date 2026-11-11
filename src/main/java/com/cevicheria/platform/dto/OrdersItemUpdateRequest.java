package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersItemUpdateRequest(BigDecimal quantity, String preparationNote) {}
