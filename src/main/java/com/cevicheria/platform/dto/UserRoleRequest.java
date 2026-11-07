package com.cevicheria.platform.dto;



public record UserRoleRequest(
        Long userBusinessId,
        Long roleId,
        Boolean active
) {}
