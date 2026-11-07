package com.cevicheria.platform.dto;

public record BusinessUserCreateRequest(
        String userName,
        String email,
        String newPassword,
        Long employeeId,
        Long branchId,
        Long roleId
) {}
