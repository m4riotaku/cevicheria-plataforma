package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.PurchaseOrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PurchaseOrderResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long supplierId,
        Long warehouseId,
        String number,
        LocalDate recordDate,
        LocalDate expectedDeliveryDate,
        String currencyCode,
        PurchaseOrderStatus status,
        Long approvedByUserBusinessId,
        LocalDateTime approvedAt,
        BigDecimal total,
        String notes,
        LocalDateTime createdAt) {
}
