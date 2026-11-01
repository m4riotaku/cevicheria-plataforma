package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SupplyScheduleRequest(
        Long businessId,
        Long branchId,
        Long warehouseId,
        Long ingredientId,
        Long supplierId,
        Long alternateSupplierId,
        BigDecimal baseQuantity,
        Integer frequencyDays,
        LocalDate nextDate,
        Boolean active) {
}
