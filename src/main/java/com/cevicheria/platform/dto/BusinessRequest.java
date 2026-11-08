package com.cevicheria.platform.dto;

public record BusinessRequest(
        String name,
        String legalName,
        String ruc,
        String currencyCode,
        String businessFormat,
        Boolean active) {
}
