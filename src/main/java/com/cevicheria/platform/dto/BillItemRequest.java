package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record BillItemRequest(
        Long businessId,
        Long branchId,
        Long orderBillId,
        Long orderItemId,
        BigDecimal quantity,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total
) {}
