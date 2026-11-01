package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryZoneResponse(
        Long id,
        Long businessId,
        Long branchId,
        String name,
        boolean active,
        BigDecimal fee,
        BigDecimal minimumOrderAmount,
        Integer estimatedMinutes,
        LocalDateTime createdAt) {
}
