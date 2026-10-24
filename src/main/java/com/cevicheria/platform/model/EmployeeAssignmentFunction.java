package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import com.cevicheria.platform.model.id.EmployeeAssignmentFunctionId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeAssignmentFunction {

    @EmbeddedId
    private EmployeeAssignmentFunctionId id;

    @MapsId("businessId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @MapsId("assignmentId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id")
    private EmployeeBranchAssignment assignment;

    @MapsId("functionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "function_id")
    private OperationalFunction function;

    public EmployeeAssignmentFunction(
            Business business,
            EmployeeBranchAssignment assignment,
            OperationalFunction function) {
        this.id = new EmployeeAssignmentFunctionId(null, null, null);
        changeBusiness(business);
        changeAssignment(assignment);
        changeFunction(function);
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        if (business.getId() == null) {
            throw new InvalidEntityDataException("El negocio debe estar registrado antes de realizar la asociación");
        }
        this.business = business;
        this.id = new EmployeeAssignmentFunctionId(business.getId(), this.id.getAssignmentId(), this.id.getFunctionId());
    }

    private void changeAssignment(EmployeeBranchAssignment assignment) {
        if (assignment == null) {
            throw new InvalidEntityDataException("La asignación del empleado al local es obligatoria");
        }
        if (assignment.getId() == null) {
            throw new InvalidEntityDataException("La asignación del empleado al local debe estar registrada antes de realizar la asociación");
        }
        this.assignment = assignment;
        this.id = new EmployeeAssignmentFunctionId(this.id.getBusinessId(), assignment.getId(), this.id.getFunctionId());
    }

    private void changeFunction(OperationalFunction function) {
        if (function == null) {
            throw new InvalidEntityDataException("La función operativa es obligatoria");
        }
        if (function.getId() == null) {
            throw new InvalidEntityDataException("La función operativa debe estar registrada antes de realizar la asociación");
        }
        this.function = function;
        this.id = new EmployeeAssignmentFunctionId(this.id.getBusinessId(), this.id.getAssignmentId(), function.getId());
    }
}
