package com.cevicheria.platform.dto;



public record BillRequest(
        Long businessId,
        Long branchId,
        Long orderId,
        Long customerId,
        Integer number,
        String name
) {}
