package com.cevicheria.platform.dto;



public record UserBusinessResponse(
        Long id,
        Long businessId,
        Long userId,
        String userName,
        String email,
        Long employeeId,
        boolean owner,
        boolean active
) {}
