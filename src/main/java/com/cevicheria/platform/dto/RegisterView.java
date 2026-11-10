package com.cevicheria.platform.dto;



public record RegisterView(
        Long id,
        Long businessId,
        Long branchId,
        String name,
        Long cashAccountId,
        boolean active
) {}
