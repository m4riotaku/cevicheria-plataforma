package com.cevicheria.platform.dto;



public record ReceiptRequest(
        Long businessId,
        Long branchId,
        Long receiptSeriesId,
        Long orderBillId,
        Long customerId,
        String currencyCode
) {}
