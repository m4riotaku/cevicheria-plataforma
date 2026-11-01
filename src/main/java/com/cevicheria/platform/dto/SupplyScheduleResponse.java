package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SupplyScheduleResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long warehouseId,
        Long ingredientId,
        Long supplierId,
        Long alternateSupplierId,
        BigDecimal baseQuantity,
        Integer frequencyDays,
        LocalDate nextDate,
        boolean active,
        LocalDateTime createdAt) {
}
