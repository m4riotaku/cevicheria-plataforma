package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;
import java.math.BigDecimal;

public record OrdersView(Long id, Long businessId, Long branchId, String number, SalesChannel salesChannel, OrderStatus status, Long customerId, Long tableId, Integer guestCount, BigDecimal subtotal, BigDecimal discount, BigDecimal tax, BigDecimal deliveryFee, BigDecimal total, String notes) {}
