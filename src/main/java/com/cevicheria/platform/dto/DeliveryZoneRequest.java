package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record DeliveryZoneRequest(
        Long businessId,
        Long branchId,
        String name,
        Boolean active,
        BigDecimal fee,
        BigDecimal minimumOrderAmount,
        Integer estimatedMinutes) {
}
