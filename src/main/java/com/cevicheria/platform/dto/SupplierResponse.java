package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

public record SupplierResponse(
        Long id,
        Long businessId,
        String name,
        String documentType,
        String documentNumber,
        String contactName,
        String phone,
        String email,
        String address,
        Integer creditDays,
        boolean active,
        LocalDateTime createdAt) {
}
