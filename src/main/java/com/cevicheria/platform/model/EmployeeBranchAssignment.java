package com.cevicheria.platform.model;

import com.cevicheria.platform.handlerException.InvalidEntityDataException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeBranchAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean active = true;

    @Generated(event = EventType.INSERT)
    private LocalDateTime assignedAt;

    @Generated(event = EventType.INSERT)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public EmployeeBranchAssignment(
            Business business,
            Branch branch,
            Employee employee) {
        changeBusiness(business);
        changeBranch(branch);
        changeEmployee(employee);
        if (business.getId() != null && branch.getBusiness() != null
                && branch.getBusiness().getId() != null
                && !business.getId().equals(branch.getBusiness().getId())) {
            throw new InvalidEntityDataException("El local no pertenece al negocio seleccionado");
        }
    }

    private void changeBusiness(Business business) {
        if (business == null) {
            throw new InvalidEntityDataException("El negocio es obligatorio");
        }
        this.business = business;
    }

    private void changeBranch(Branch branch) {
        if (branch == null) {
            throw new InvalidEntityDataException("El local es obligatorio");
        }
        this.branch = branch;
    }

    private void changeEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidEntityDataException("El empleado es obligatorio");
        }
        this.employee = employee;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

}
