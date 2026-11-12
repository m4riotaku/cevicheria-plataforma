package com.cevicheria.platform.dto;

import java.time.LocalDateTime;

public record BranchResponse(
        Long id,
        Long businessId,
        String name,
        String address,
        boolean active,
        String ubigeoCode,
        String department,
        String province,
        String district,
        String phone,
        String timezone,
        boolean dineInEnabled,
        boolean takeoutEnabled,
        boolean deliveryEnabled,
        boolean reservationsEnabled,
        boolean publicMenuEnabled,
        String menuSlug,
        Long responsibleEmployeeId,
        LocalDateTime createdAt) {
}
