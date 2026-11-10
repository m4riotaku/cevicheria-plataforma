package com.cevicheria.platform.dto;

import java.math.BigDecimal;
import com.cevicheria.platform.model.enums.SalesReceiptStates;

public record ReceiptView(
        Long id,
        Long businessId,
        Long branchId,
        Long receiptSeriesId,
        Long number,
        Long orderBillId,
        SalesReceiptStates status,
        BigDecimal subtotal,
        BigDecimal discount,
        BigDecimal tax,
        BigDecimal total
) {}
