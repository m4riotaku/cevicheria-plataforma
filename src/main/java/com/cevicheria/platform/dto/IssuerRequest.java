package com.cevicheria.platform.dto;



public record IssuerRequest(
        Long businessId,
        Long branchId,
        Long salesReceiptId,
        String legalName,
        String ruc,
        String address,
        String ubigeoCode
) {}
