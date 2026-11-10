package com.cevicheria.platform.dto;



public record CustomerSnapshotRequest(
        Long businessId,
        Long branchId,
        Long salesReceiptId,
        String customerName,
        String documentType,
        String documentNumber,
        String address
) {}
