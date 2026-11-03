package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationRequest(
        Long businessId,
        Long branchId,
        Long diningTableId,
        String contactName,
        String phone,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer guestCount,
        Integer toleranceMinutes,
        BigDecimal requiredDeposit,
        ReservationStatus status,
        String notes) {
}
