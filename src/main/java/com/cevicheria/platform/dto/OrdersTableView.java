package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.*;

public record OrdersTableView(Long id, Long businessId, Long branchId, String number, String zone, Integer capacity, DiningTableStatus status) {}
