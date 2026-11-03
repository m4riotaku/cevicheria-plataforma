package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long customerId,
        Long diningTableId,
        String contactName,
        String phone,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer guestCount,
        Integer toleranceMinutes,
        BigDecimal requiredDeposit,
        ReservationStatus status,
        String notes,
        LocalDateTime createdAt) {
}
