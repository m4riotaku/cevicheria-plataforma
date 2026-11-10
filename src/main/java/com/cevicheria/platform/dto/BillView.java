package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.OrderBillStates;

public record BillView(
        Long id,
        Long businessId,
        Long branchId,
        Long orderId,
        Integer number,
        OrderBillStates status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal deliveryFee,
        BigDecimal total
) {}
