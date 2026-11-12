package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersModifierRequest(Long businessId, Long branchId, Long orderItemId, Long modifierId, BigDecimal quantity) {}
