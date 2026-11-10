package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.PaymentDirection;
import com.cevicheria.platform.model.enums.PaymentMethod;
import com.cevicheria.platform.model.enums.PaymentStates;

public record PaymentView(
        Long id,
        Long businessId,
        Long branchId,
        String operationKey,
        PaymentMethod method,
        PaymentDirection direction,
        PaymentStates status,
        BigDecimal amount,
        BigDecimal tip,
        BigDecimal amountReceived,
        BigDecimal changeAmount
) {}
