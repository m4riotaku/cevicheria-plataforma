package com.cevicheria.platform.dto;



public record UserBusinessRequest(
        Long userId,
        Long employeeId,
        Boolean owner,
        Boolean active
) {}
