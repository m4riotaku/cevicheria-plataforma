package com.cevicheria.platform.dto;

public record OrdersUpdateRequest(Long customerId, Integer guestCount, String contactName, String contactPhone, String deliveryAddress, String notes) {}
