package com.cevicheria.platform.dto;



public record AttemptView(
        Long id,
        Long salesReceiptId,
        String provider,
        String responseCode,
        String responseMessage
) {}
