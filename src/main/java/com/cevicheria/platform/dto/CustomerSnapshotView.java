package com.cevicheria.platform.dto;



public record CustomerSnapshotView(
        Long salesReceiptId,
        String customerName,
        String documentType,
        String documentNumber,
        String address
) {}
