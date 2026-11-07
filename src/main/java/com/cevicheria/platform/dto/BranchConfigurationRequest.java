package com.cevicheria.platform.dto;

public record BranchConfigurationRequest(
        Boolean saleByWeightEnabled,
        Boolean stockAlertEnabled,
        Integer expirationAlertDays) {
}
