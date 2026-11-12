package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersItemRequest(Long businessId, Long branchId, Long orderId, Long productId, BigDecimal quantity, String preparationNote) {}
