package com.cevicheria.platform.dto;

public record OrdersAreaRequest(Long businessId, Long branchId, String name, String printerDestination) {}
