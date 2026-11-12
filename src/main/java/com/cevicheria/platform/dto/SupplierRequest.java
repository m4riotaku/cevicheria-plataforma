package com.cevicheria.platform.dto;



public record SupplierRequest(
        Long businessId,
        String name,
        String documentType,
        String documentNumber,
        String contactName,
        String phone,
        String email,
        String address,
        Integer creditDays,
        Boolean active) {
}
