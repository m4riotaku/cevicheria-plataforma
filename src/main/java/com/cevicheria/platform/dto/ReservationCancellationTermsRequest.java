package com.cevicheria.platform.dto;
import java.math.BigDecimal;

public record ReservationCancellationTermsRequest(Integer fullRefundBeforeHours,
        BigDecimal noShowRetentionAmount, String policyNotes) {}
