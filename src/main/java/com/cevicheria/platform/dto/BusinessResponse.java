package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

public record BusinessResponse(
        Long id,
        String name,
        String legalName,
        String ruc,
        String currencyCode,
        String businessFormat,
        boolean active,
        LocalDateTime createdAt) {
}
