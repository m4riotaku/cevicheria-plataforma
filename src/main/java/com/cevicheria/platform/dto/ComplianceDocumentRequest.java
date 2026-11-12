package com.cevicheria.platform.dto;

import java.time.LocalDate;
import com.cevicheria.platform.model.enums.DocumentStates;

public record ComplianceDocumentRequest(
        Long employeeId,
        Long supplierId,
        Long branchId,
        Long previousDocumentId,
        String type,
        String number,
        LocalDate issueDate,
        LocalDate expirationDate,
        LocalDate reviewDate,
        String fileUrl,
        DocumentStates status,
        String notes
) {}
