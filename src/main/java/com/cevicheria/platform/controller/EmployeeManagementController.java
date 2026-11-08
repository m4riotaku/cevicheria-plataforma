package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.EmployeeBranchAssignmentRequest;
import com.cevicheria.platform.dto.EmployeeBranchAssignmentResponse;
import com.cevicheria.platform.dto.EmployeeRequest;
import com.cevicheria.platform.dto.EmployeeResponse;
import com.cevicheria.platform.service.EmployeeManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/businesses/{businessId}")
public class EmployeeManagementController
{

    private final EmployeeManagementService service;

    @GetMapping("/employees")
    public List<EmployeeResponse> list(@PathVariable Long businessId)
    {
        return service.list(businessId);
    }

    @GetMapping("/branches/{branchId}/employees")
    public List<EmployeeResponse> listBranch(@PathVariable Long businessId, @PathVariable Long branchId)
    {
        return service.listBranch(businessId, branchId);
    }

    @GetMapping("/employees/{id}")
    public EmployeeResponse find(@PathVariable Long businessId, @PathVariable Long id)
    {
        return service.find(businessId, id);
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> create(@PathVariable Long businessId, @RequestBody EmployeeRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(businessId, request));
    }

    @PatchMapping("/employees/{id}")
    public EmployeeResponse update(@PathVariable Long businessId, @PathVariable Long id, @RequestBody EmployeeRequest request)
    {
        return service.update(businessId, id, request);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long businessId, @PathVariable Long id)
    {
        service.deactivate(businessId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branches/{branchId}/employee-assignments")
    public List<EmployeeBranchAssignmentResponse> assignments(@PathVariable Long businessId, @PathVariable Long branchId)
    {
        return service.assignments(businessId, branchId);
    }

    @GetMapping("/branches/{branchId}/employee-assignments/{id}")
    public EmployeeBranchAssignmentResponse assignment(@PathVariable Long businessId,
                               @PathVariable Long branchId, @PathVariable Long id)
    {
        return service.assignment(businessId, branchId, id);
    }

    @PostMapping("/branches/{branchId}/employee-assignments")
    public ResponseEntity<EmployeeBranchAssignmentResponse> assign(@PathVariable Long businessId, @PathVariable Long branchId, @RequestBody EmployeeBranchAssignmentRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assign(businessId, branchId, request));
    }

    @PatchMapping("/branches/{branchId}/employee-assignments/{id}")
    public EmployeeBranchAssignmentResponse changeAssignment(@PathVariable Long businessId,
                @PathVariable Long branchId, @PathVariable Long id, @RequestBody EmployeeBranchAssignmentRequest request)
    {
        return service.changeAssignment(businessId, branchId, id, request);
    }

    @DeleteMapping("/branches/{branchId}/employee-assignments/{id}")
    public ResponseEntity<Void> deactivateAssignment(@PathVariable Long businessId,
                                                      @PathVariable Long branchId, @PathVariable Long id)
    {
        service.deactivateAssignment(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branches/{branchId}/employee-assignments/{assignmentId}/functions")
    public List<Long> functions(@PathVariable Long businessId, @PathVariable Long branchId, @PathVariable Long assignmentId)
    {
        return service.functions(businessId, branchId, assignmentId);
    }

    @PutMapping("/branches/{branchId}/employee-assignments/{assignmentId}/functions/{functionId}")
    public ResponseEntity<Void> addFunction(@PathVariable Long businessId, @PathVariable Long branchId, @PathVariable Long assignmentId, @PathVariable Long functionId)
    {
        service.addFunction(businessId, branchId, assignmentId, functionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/branches/{branchId}/employee-assignments/{assignmentId}/functions/{functionId}")
    public ResponseEntity<Void> removeFunction(@PathVariable Long businessId, @PathVariable Long branchId, @PathVariable Long assignmentId, @PathVariable Long functionId)
    {
        service.removeFunction(businessId, branchId, assignmentId, functionId);
        return ResponseEntity.noContent().build();
    }
}
