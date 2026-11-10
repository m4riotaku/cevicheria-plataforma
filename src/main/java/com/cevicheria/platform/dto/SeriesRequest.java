package com.cevicheria.platform.dto;



public record SeriesRequest(
        Long businessId,
        Long branchId,
        String type,
        String series,
        Boolean active
) {}
