package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.DeliveryStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DeliveryResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long orderId,
        Long deliveryZoneId,
        Long driverEmployeeId,
        String driverName,
        String driverPhone,
        String address,
        BigDecimal deliveryFeeSnapshot,
        Integer estimatedMinutes,
        BigDecimal cashOnDeliveryAmount,
        LocalDateTime dispatchedAt,
        LocalDateTime deliveredAt,
        DeliveryStatus status,
        String addressReference,
        LocalDateTime createdAt) {
}
