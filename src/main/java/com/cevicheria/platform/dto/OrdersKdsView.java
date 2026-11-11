package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;

public record OrdersKdsView(Long preparationAreaId, KdsSortOrder sortOrder, Integer alertMinutes) {}
