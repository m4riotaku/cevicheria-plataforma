package com.cevicheria.platform.dto;

public record OrdersAreaView(Long id, Long businessId, Long branchId, String name, boolean active, String printerDestination) {}
