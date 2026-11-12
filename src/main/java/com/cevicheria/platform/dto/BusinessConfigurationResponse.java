package com.cevicheria.platform.dto;

import java.time.LocalDateTime;
import com.cevicheria.platform.model.enums.InvoiceEnvironment;

public record BusinessConfigurationResponse(
        Long businessId,
        boolean reservationsEnabled,
        InvoiceEnvironment invoiceEnvironment,
        String billingProvider,
        LocalDateTime createdAt) {
}
