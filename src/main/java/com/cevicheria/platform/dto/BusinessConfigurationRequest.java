package com.cevicheria.platform.dto;

import com.cevicheria.platform.model.enums.InvoiceEnvironment;

public record BusinessConfigurationRequest(
        Boolean reservationsEnabled,
        InvoiceEnvironment invoiceEnvironment,
        String billingProvider) {
}
