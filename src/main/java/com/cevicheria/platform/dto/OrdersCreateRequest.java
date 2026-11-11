package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;

public record OrdersCreateRequest(Long businessId, Long branchId, String number, String requestKey, SalesChannel salesChannel, Long customerId, Long tableId, Long waiterId, Integer guestCount, String contactName, String contactPhone, String deliveryAddress, String notes) {}
