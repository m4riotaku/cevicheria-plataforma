package com.cevicheria.platform.dto;



public record SecurityRoleResponse(
        Long id,
        Long businessId,
        String name,
        boolean active
) {}
