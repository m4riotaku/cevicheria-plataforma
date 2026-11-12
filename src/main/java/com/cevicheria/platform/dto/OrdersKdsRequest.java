package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;

public record OrdersKdsRequest(KdsSortOrder sortOrder, Integer alertMinutes) {}
