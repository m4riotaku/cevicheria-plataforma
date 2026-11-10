package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record BillItemView(
        Long id,
        Long businessId,
        Long branchId,
        Long orderBillId,
        Long orderItemId,
        BigDecimal quantity,
        BigDecimal total
) {}
