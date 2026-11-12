package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;
import java.math.BigDecimal;

public record OrdersItemView(Long id, Long orderId, Long productId, Long preparationAreaId, String description, BigDecimal quantity, BigDecimal unitPrice, BigDecimal subtotal, BigDecimal tax, BigDecimal total, OrderItemStatus status) {}
