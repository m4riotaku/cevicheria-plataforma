package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.ComplianceDocumentRequest;
import com.cevicheria.platform.dto.ComplianceDocumentResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.ComplianceDocument;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.model.Supplier;
import com.cevicheria.platform.model.enums.DocumentStates;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.ComplianceDocumentRepository;
import com.cevicheria.platform.repository.EmployeeBranchAssignmentRepository;
import com.cevicheria.platform.repository.EmployeeRepository;
import com.cevicheria.platform.repository.SupplierRepository;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.ComplianceDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComplianceDocumentServiceImpl implements ComplianceDocumentService {

    private final UsersAccessService access;
    private final ComplianceDocumentRepository documents;
    private final BusinessRepository businesses;
    private final EmployeeRepository employees;
    private final SupplierRepository suppliers;
    private final BranchRepository branches;
    private final EmployeeBranchAssignmentRepository assignments;

    @Override
    public List<ComplianceDocumentResponse> list(Long businessId) {
        access.requireBusinessOwner(businessId);
        return documents.findByBusinessIdOrderByIdAsc(businessId).stream().map(this::response).toList();
    }

    @Override
    public List<ComplianceDocumentResponse> listBranch(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
        return documents.findVisibleByBranch(businessId, branchId)
                .stream().map(this::response).toList();
    }

    @Override
    public ComplianceDocumentResponse findBranch(Long businessId, Long branchId, Long id) {
        access.requireBranchRead(businessId, branchId);
        return documents.findVisibleByBranchAndId(businessId, branchId, id)
                .map(this::response)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceDocument", "ID", id));
    }

    @Override
    public ComplianceDocumentResponse find(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        return response(document(businessId, id));
    }

    @Override
    @Transactional
    public ComplianceDocumentResponse create(Long businessId, ComplianceDocumentRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos de documento obligatorios");
        Employee employee = request.employeeId() == null ? null : employees.findByIdAndBusinessId(request.employeeId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", request.employeeId()));
        Supplier supplier = request.supplierId() == null ? null : suppliers.findByIdAndBusinessId(request.supplierId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "ID", request.supplierId()));
        Branch branch = request.branchId() == null ? null : branches.findByIdAndBusinessId(request.branchId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", request.branchId()));
        ComplianceDocument doc = new ComplianceDocument(business(businessId), employee, supplier, branch, request.type());
        apply(businessId, doc, request, false);
        return response(documents.save(doc));
    }

    @Override
    @Transactional
    public ComplianceDocumentResponse update(Long businessId, Long id, ComplianceDocumentRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos obligatorios");
        ComplianceDocument doc = document(businessId, id);
        if (request.employeeId() != null && (doc.getEmployee() == null || !doc.getEmployee().getId().equals(request.employeeId()))) {
            throw new BadRequestException("No se puede cambiar el titular de un documento; crea otro registro");
        }
        if (request.supplierId() != null && (doc.getSupplier() == null || !doc.getSupplier().getId().equals(request.supplierId()))) {
            throw new BadRequestException("No se puede cambiar el titular de un documento; crea otro registro");
        }
        if (request.branchId() != null && (doc.getBranch() == null || !doc.getBranch().getId().equals(request.branchId()))) {
            throw new BadRequestException("No se puede cambiar el titular de un documento; crea otro registro");
        }
        apply(businessId, doc, request, true);
        return response(doc);
    }

    @Override
    @Transactional
    public void annul(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        document(businessId, id).changeStatus(DocumentStates.ANULADO);
    }

    private void apply(Long businessId, ComplianceDocument doc, ComplianceDocumentRequest request, boolean patch) {
        if (!patch || request.type() != null) doc.changeType(request.type());
        if (request.number() != null) doc.changeNumber(request.number());
        if (request.issueDate() != null || request.expirationDate() != null) {
            doc.changeDates(request.issueDate() == null ? doc.getIssueDate() : request.issueDate(),
                    request.expirationDate() == null ? doc.getExpirationDate() : request.expirationDate());
        }
        if (request.reviewDate() != null) doc.changeReviewDate(request.reviewDate());
        if (request.fileUrl() != null) doc.changeFileUrl(request.fileUrl());
        if (request.status() != null) doc.changeStatus(request.status());
        if (request.notes() != null) doc.changeNotes(request.notes());
        if (request.previousDocumentId() != null) {
            ComplianceDocument prev = document(businessId, request.previousDocumentId());
            if (doc.getId() != null && doc.getId().equals(prev.getId())) {
                throw new BadRequestException("Un documento no puede ser su propio antecedente");
            }
            // Evitar ciclos en la cadena de reemplazos.
            ComplianceDocument cursor = prev;
            Set<Long> visited = new HashSet<>();
            while (cursor != null) {
                if (cursor.getId() != null && !visited.add(cursor.getId())) {
                    throw new BadRequestException("Cadena de documentos circular");
                }
                if (doc.getId() != null && doc.getId().equals(cursor.getId())) {
                    throw new BadRequestException("La referencia anterior crea un ciclo");
                }
                cursor = cursor.getPreviousDocument();
            }
            doc.changePreviousDocument(prev);
        }
    }

    private Business business(Long id) {
        return businesses.findById(id).orElseThrow(() -> new ResourceNotFoundException("Business", "ID", id));
    }
    private ComplianceDocument document(Long businessId, Long id) {
        return documents.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("ComplianceDocument", "ID", id));
    }
    private ComplianceDocumentResponse response(ComplianceDocument d) {
        return new ComplianceDocumentResponse(d.getId(), d.getBusiness().getId(),
                d.getEmployee() == null ? null : d.getEmployee().getId(),
                d.getSupplier() == null ? null : d.getSupplier().getId(),
                d.getBranch() == null ? null : d.getBranch().getId(),
                d.getPreviousDocument() == null ? null : d.getPreviousDocument().getId(),
                d.getType(), d.getNumber(), d.getIssueDate(), d.getExpirationDate(),
                d.getReviewDate(), d.getFileUrl(), d.getStatus(), d.getNotes());
    }
}
