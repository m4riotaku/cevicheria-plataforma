package com.cevicheria.platform.dto;
public record BusinessRoleResponse(Long id, Long businessId, Long userBusinessId,
                                   Long roleId, String roleName, boolean active) { }
