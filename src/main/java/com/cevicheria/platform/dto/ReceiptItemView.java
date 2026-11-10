package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record ReceiptItemView(
        Long id,
        Long salesReceiptId,
        String description,
        String unit,
        BigDecimal quantity,
        BigDecimal unitValue,
        BigDecimal total
) {}
