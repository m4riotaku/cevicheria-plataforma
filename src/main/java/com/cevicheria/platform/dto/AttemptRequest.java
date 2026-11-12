package com.cevicheria.platform.dto;



public record AttemptRequest(
        Long businessId,
        Long branchId,
        Long salesReceiptId,
        String provider,
        String externalOperationId,
        String requestReference,
        String responseCode,
        String responseMessage
) {}
