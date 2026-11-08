package com.cevicheria.platform.dto;

public record BranchRequest(
        String name,
        String address,
        Boolean active,
        String ubigeoCode,
        String department,
        String province,
        String district,
        String phone,
        String timezone,
        Boolean dineInEnabled,
        Boolean takeoutEnabled,
        Boolean deliveryEnabled,
        Boolean reservationsEnabled,
        Boolean publicMenuEnabled,
        String menuSlug,
        Long responsibleEmployeeId) {
}
