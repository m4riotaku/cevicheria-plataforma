package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;

public record OrdersKitchenRequest(OrderItemStatus status, Long preparedByEmployeeId) {}
