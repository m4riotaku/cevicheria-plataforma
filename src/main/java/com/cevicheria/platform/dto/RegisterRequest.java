package com.cevicheria.platform.dto;



public record RegisterRequest(
        Long businessId,
        Long branchId,
        String name,
        Long cashAccountId,
        Boolean active
) {}
