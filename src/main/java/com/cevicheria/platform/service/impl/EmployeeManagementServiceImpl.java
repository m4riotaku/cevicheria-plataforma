package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.EmployeeBranchAssignmentRequest;
import com.cevicheria.platform.dto.EmployeeBranchAssignmentResponse;
import com.cevicheria.platform.dto.EmployeeRequest;
import com.cevicheria.platform.dto.EmployeeResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.model.EmployeeAssignmentFunction;
import com.cevicheria.platform.model.EmployeeBranchAssignment;
import com.cevicheria.platform.model.OperationalFunction;
import com.cevicheria.platform.model.id.EmployeeAssignmentFunctionId;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.EmployeeAssignmentFunctionRepository;
import com.cevicheria.platform.repository.EmployeeBranchAssignmentRepository;
import com.cevicheria.platform.repository.EmployeeRepository;
import com.cevicheria.platform.repository.OperationalFunctionRepository;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.EmployeeManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeManagementServiceImpl implements EmployeeManagementService {

    private final UsersAccessService access;
    private final BusinessRepository businesses;
    private final BranchRepository branches;
    private final EmployeeRepository employees;
    private final EmployeeBranchAssignmentRepository assignments;
    private final OperationalFunctionRepository functions;
    private final EmployeeAssignmentFunctionRepository assignmentFunctions;

    @Override
    public List<EmployeeResponse> list(Long businessId) {
        access.requireBusinessOwner(businessId);
        return employees.findByBusinessIdOrderByIdAsc(businessId).stream().map(this::response).toList();
    }

    @Override
    public List<EmployeeResponse> listBranch(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
        return assignments.findByBusinessIdAndBranchIdOrderByIdAsc(businessId, branchId)
                .stream().filter(EmployeeBranchAssignment::isActive)
                .map(EmployeeBranchAssignment::getEmployee)
                .filter(Employee::isActive)
                .map(this::response).toList();
    }

    @Override
    public EmployeeResponse find(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        return response(employee(businessId, id));
    }

    @Override
    @Transactional
    public EmployeeResponse create(Long businessId, EmployeeRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos del empleado obligatorios");
        Business business = businesses.findById(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", businessId));
        Employee entity = new Employee(business, request.firstNames(), request.lastNames(),
                request.documentType(), request.documentNumber(), request.hireDate());
        apply(entity, request, false);
        if (employees.existsByBusinessIdAndDocumentTypeAndDocumentNumber(
                businessId, entity.getDocumentType(), entity.getDocumentNumber())) {
            throw new ResourceConflictException("El documento ya pertenece a un empleado del negocio");
        }
        return response(employees.save(entity));
    }

    @Override
    @Transactional
    public EmployeeResponse update(Long businessId, Long id, EmployeeRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos del empleado obligatorios");
        Employee entity = employee(businessId, id);
        apply(entity, request, true);
        if (employees.existsByBusinessIdAndDocumentTypeAndDocumentNumberAndIdNot(
                businessId, entity.getDocumentType(), entity.getDocumentNumber(), id)) {
            throw new ResourceConflictException("El documento ya pertenece a otro empleado");
        }
        return response(employees.save(entity));
    }

    @Override
    @Transactional
    public void deactivate(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        employee(businessId, id).deactivate();
    }

    @Override
    public List<EmployeeBranchAssignmentResponse> assignments(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
        return assignments.findByBusinessIdAndBranchIdOrderByIdAsc(businessId, branchId)
                .stream().map(this::assignmentResponse).toList();
    }

    @Override
    public EmployeeBranchAssignmentResponse assignment(Long businessId, Long branchId, Long assignmentId) {
        access.requireBranchRead(businessId, branchId);
        return assignmentResponse(findAssignment(businessId, branchId, assignmentId));
    }

    @Override
    @Transactional
    public EmployeeBranchAssignmentResponse assign(Long businessId, Long branchId,
                                                   EmployeeBranchAssignmentRequest request) {
        access.requireBranchManage(businessId, branchId);
        if (request == null || request.employeeId() == null) {
            throw new BadRequestException("Se requiere employeeId");
        }
        if (assignments.existsByBusinessIdAndBranchIdAndEmployeeId(
                businessId, branchId, request.employeeId())) {
            throw new ResourceConflictException("El empleado ya tiene asignación en la sucursal");
        }
        Business business = business(businessId);
        Branch branch = branch(businessId, branchId);
        Employee employee = employee(businessId, request.employeeId());
        if (!employee.isActive() || !branch.isActive()) {
            throw new BadRequestException("El empleado y la sucursal deben estar activos");
        }
        EmployeeBranchAssignment assignment = new EmployeeBranchAssignment(business, branch, employee);
        if (Boolean.FALSE.equals(request.active())) assignment.deactivate();
        return assignmentResponse(assignments.save(assignment));
    }

    @Override
    @Transactional
    public EmployeeBranchAssignmentResponse changeAssignment(Long businessId, Long branchId, Long id,
                                                             EmployeeBranchAssignmentRequest request) {
        access.requireBranchManage(businessId, branchId);
        if (request == null || request.active() == null) {
            throw new BadRequestException("Se requiere active");
        }
        EmployeeBranchAssignment item = findAssignment(businessId, branchId, id);
        if (request.employeeId() != null && !request.employeeId().equals(item.getEmployee().getId())) {
            throw new BadRequestException("No puedes cambiar el empleado; crea otra asignación");
        }
        if (request.active()) {
            if (!item.getEmployee().isActive() || !item.getBranch().isActive()) {
                throw new BadRequestException("El empleado y la sucursal deben estar activos");
            }
            item.activate();
        } else {
            item.deactivate();
        }
        return assignmentResponse(item);
    }

    @Override
    @Transactional
    public void deactivateAssignment(Long businessId, Long branchId, Long id) {
        access.requireBranchManage(businessId, branchId);
        findAssignment(businessId, branchId, id).deactivate();
    }

    @Override
    public List<Long> functions(Long businessId, Long branchId, Long assignmentId) {
        access.requireBranchRead(businessId, branchId);
        findAssignment(businessId, branchId, assignmentId);
        return assignmentFunctions.findByIdBusinessIdAndIdAssignmentIdOrderByIdFunctionIdAsc(
                businessId, assignmentId).stream().map(item -> item.getFunction().getId()).toList();
    }

    @Override
    @Transactional
    public void addFunction(Long businessId, Long branchId, Long assignmentId, Long functionId) {
        access.requireBranchManage(businessId, branchId);
        EmployeeBranchAssignment assignment = findAssignment(businessId, branchId, assignmentId);
        validateActiveAssignment(assignment);
        OperationalFunction function = functions.findById(functionId)
                .orElseThrow(() -> new ResourceNotFoundException("OperationalFunction", "ID", functionId));
        EmployeeAssignmentFunctionId id = new EmployeeAssignmentFunctionId(businessId, assignmentId, functionId);
        if (assignmentFunctions.existsById(id)) return;
        assignmentFunctions.save(new EmployeeAssignmentFunction(business(businessId), assignment, function));
    }

    @Override
    @Transactional
    public void removeFunction(Long businessId, Long branchId, Long assignmentId, Long functionId) {
        access.requireBranchManage(businessId, branchId);
        findAssignment(businessId, branchId, assignmentId);
        EmployeeAssignmentFunctionId id = new EmployeeAssignmentFunctionId(businessId, assignmentId, functionId);
        if (!assignmentFunctions.existsById(id)) throw new ResourceNotFoundException("EmployeeAssignmentFunction", "ID", id);
        assignmentFunctions.deleteById(id);
    }

    private void validateActiveAssignment(EmployeeBranchAssignment item) {
        if (!item.isActive() || !item.getEmployee().isActive() || !item.getBranch().isActive()) {
            throw new BadRequestException("El empleado y la sucursal deben estar activos");
        }
    }

    private void apply(Employee entity, EmployeeRequest r, boolean patch) {
        if (!patch || r.firstNames() != null) entity.changeFirstNames(r.firstNames());
        if (!patch || r.lastNames() != null) entity.changeLastNames(r.lastNames());
        if (!patch || r.documentType() != null) entity.changeDocumentType(r.documentType());
        if (!patch || r.documentNumber() != null) entity.changeDocumentNumber(r.documentNumber());
        if (!patch || r.hireDate() != null) entity.changeHireDate(r.hireDate());
        if (r.phone() != null) entity.changePhone(r.phone());
        if (r.email() != null) entity.changeEmail(r.email());
        if (r.terminationDate() != null) entity.changeTerminationDate(r.terminationDate());
        if (r.notes() != null) entity.changeNotes(r.notes());
        if (Boolean.TRUE.equals(r.active())) entity.activate();
        if (Boolean.FALSE.equals(r.active())) entity.deactivate();
        if (entity.getTerminationDate() != null
                && entity.getTerminationDate().isBefore(entity.getHireDate())) {
            throw new BadRequestException("La fecha de cese no puede ser anterior a la contratación");
        }
    }

    private Business business(Long id) {
        return businesses.findById(id).orElseThrow(() -> new ResourceNotFoundException("Business", "ID", id));
    }
    private Branch branch(Long businessId, Long id) {
        return branches.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", id));
    }
    private Employee employee(Long businessId, Long id) {
        return employees.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", id));
    }
    private EmployeeBranchAssignment findAssignment(Long businessId, Long branchId, Long id) {
        EmployeeBranchAssignment value = assignments.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeBranchAssignment", "ID", id));
        if (!value.getBranch().getId().equals(branchId)) {
            throw new ResourceNotFoundException("EmployeeBranchAssignment", "ID", id);
        }
        return value;
    }
    private EmployeeResponse response(Employee e) {
        return new EmployeeResponse(e.getId(), e.getBusiness().getId(), e.getFirstNames(),
                e.getLastNames(), e.getDocumentType(), e.getDocumentNumber(), e.getHireDate(),
                e.getTerminationDate(), e.getPhone(), e.getEmail(), e.isActive(), e.getNotes());
    }
    private EmployeeBranchAssignmentResponse assignmentResponse(EmployeeBranchAssignment e) {
        return new EmployeeBranchAssignmentResponse(e.getId(), e.getBusiness().getId(),
                e.getBranch().getId(), e.getEmployee().getId(), e.isActive());
    }
}
