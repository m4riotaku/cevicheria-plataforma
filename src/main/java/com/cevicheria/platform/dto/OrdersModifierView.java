package com.cevicheria.platform.dto;

import java.math.BigDecimal;

public record OrdersModifierView(Long id, Long orderItemId, Long modifierId, String name, BigDecimal quantity, BigDecimal extraPrice) {}
