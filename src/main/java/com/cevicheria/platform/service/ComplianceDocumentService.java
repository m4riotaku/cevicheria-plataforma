package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.ComplianceDocumentRequest;
import com.cevicheria.platform.dto.ComplianceDocumentResponse;
import java.util.List;

public interface ComplianceDocumentService {
    List<ComplianceDocumentResponse> list(Long businessId);
    List<ComplianceDocumentResponse> listBranch(Long businessId, Long branchId);
    ComplianceDocumentResponse findBranch(Long businessId, Long branchId, Long id);
    ComplianceDocumentResponse find(Long businessId, Long id);
    ComplianceDocumentResponse create(Long businessId, ComplianceDocumentRequest request);
    ComplianceDocumentResponse update(Long businessId, Long id, ComplianceDocumentRequest request);
    void annul(Long businessId, Long id);
}
