package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record ReceiptItemRequest(
        Long businessId,
        Long branchId,
        Long salesReceiptId,
        Long orderBillItemId,
        String description,
        String unit,
        BigDecimal quantity,
        BigDecimal unitValue,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total,
        BigDecimal taxRate,
        String taxTreatmentCode
) {}
