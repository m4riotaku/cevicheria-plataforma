package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.ComplianceDocumentRequest;
import com.cevicheria.platform.dto.ComplianceDocumentResponse;
import com.cevicheria.platform.service.ComplianceDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/businesses/{businessId}")
public class ComplianceDocumentController
{
    private final ComplianceDocumentService service;

    @GetMapping("/compliance-documents")
    public List<ComplianceDocumentResponse> list(@PathVariable Long businessId)
    {
        return service.list(businessId);
    }

    @GetMapping("/branches/{branchId}/compliance-documents")
    public List<ComplianceDocumentResponse> listBranch(@PathVariable Long businessId, @PathVariable Long branchId)
    {
        return service.listBranch(businessId, branchId);
    }

    @GetMapping("/branches/{branchId}/compliance-documents/{id}")
    public ComplianceDocumentResponse findBranch(@PathVariable Long businessId,
                                                 @PathVariable Long branchId, @PathVariable Long id)
    {
        return service.findBranch(businessId, branchId, id);
    }

    @GetMapping("/compliance-documents/{id}")
    public ComplianceDocumentResponse find(@PathVariable Long businessId, @PathVariable Long id)
    {
        return service.find(businessId, id);
    }

    @PostMapping("/compliance-documents")
    public ResponseEntity<ComplianceDocumentResponse> create(@PathVariable Long businessId, @RequestBody ComplianceDocumentRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(businessId, request));
    }

    @PutMapping("/compliance-documents/{id}")
    public ComplianceDocumentResponse update(@PathVariable Long businessId, @PathVariable Long id, @RequestBody ComplianceDocumentRequest request)
    {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/compliance-documents/{id}")
    public ResponseEntity<Void> annul(@PathVariable Long businessId, @PathVariable Long id)
    {
        service.annul(businessId, id);
        return ResponseEntity.noContent().build();
    }
}
