package com.cevicheria.platform.dto;



public record IssuerView(
        Long salesReceiptId,
        String legalName,
        String ruc,
        String address,
        String ubigeoCode
) {}
