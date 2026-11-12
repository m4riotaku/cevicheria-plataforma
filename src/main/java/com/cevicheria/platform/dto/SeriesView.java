package com.cevicheria.platform.dto;



public record SeriesView(
        Long id,
        Long businessId,
        Long branchId,
        String type,
        String series,
        Long lastNumber,
        boolean active
) {}
