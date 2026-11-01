package com.cevicheria.platform.dto;
import java.math.BigDecimal;

public record ReservationCancellationTermsResponse(Long businessId, Long branchId, Long reservationId,
        Integer fullRefundBeforeHours, BigDecimal noShowRetentionAmount, String policyNotes) {}
