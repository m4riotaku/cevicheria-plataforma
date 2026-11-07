package com.cevicheria.platform.dto;



public record SecurityRoleRequest(
        String name,
        Boolean active
) {}
