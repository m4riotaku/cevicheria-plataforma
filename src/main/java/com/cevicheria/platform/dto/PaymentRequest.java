package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.PaymentConcept;
import com.cevicheria.platform.model.enums.PaymentDirection;
import com.cevicheria.platform.model.enums.PaymentMethod;

public record PaymentRequest(
        Long businessId,
        Long branchId,
        String operationKey,
        PaymentDirection direction,
        PaymentConcept concept,
        PaymentMethod paymentMethod,
        Long financialAccountId,
        Long cashShiftId,
        Long customerId,
        BigDecimal amount,
        BigDecimal tip,
        BigDecimal amountReceived,
        BigDecimal changeAmount,
        String externalReference
) {}
