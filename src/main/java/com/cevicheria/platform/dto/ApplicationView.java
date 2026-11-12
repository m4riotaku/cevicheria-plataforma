package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.PaymentApplicationStates;

public record ApplicationView(
        Long id,
        Long businessId,
        Long branchId,
        Long paymentId,
        Long orderBillId,
        BigDecimal amount,
        PaymentApplicationStates status,
        Long purchaseInstallmentId,
        Long reservationId
) {}
