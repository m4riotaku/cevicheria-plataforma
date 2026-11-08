package com.cevicheria.platform.dto;



public record UserRoleResponse(
        Long id,
        Long businessId,
        Long branchId,
        Long userBusinessId,
        Long roleId,
        String roleName,
        boolean active
) {}
