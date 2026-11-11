package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.*;
import com.cevicheria.platform.security.UsersAccessService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrdersSupport {
    private final EntityManager em;
    private final UsersAccessService access;

    public void read(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
    }

    public void write(Long businessId, Long branchId, String permission) {
        access.requireBranchPermission(businessId, branchId, permission);
    }

    public void check(boolean valid, String message) {
        if (!valid) throw new BadRequestException(message);
    }

    public void conflict(boolean invalid, String message) {
        if (invalid) throw new ResourceConflictException(message);
    }

    public <T> T require(Class<T> type, Long id) {
        check(id != null && id > 0, "El ID debe ser positivo");
        T result = em.find(type, id);
        if (result == null) throw new ResourceNotFoundException(type.getSimpleName(), "ID", id);
        return result;
    }

    public Branch branch(Long businessId, Long branchId) {
        Branch value = require(Branch.class, branchId);
        check(Objects.equals(value.getBusiness().getId(), businessId), "La sucursal no pertenece al negocio");
        check(value.isActive(), "La sucursal está inactiva");
        return value;
    }

    public Business business(Long businessId) {
        Business value = require(Business.class, businessId);
        check(value.isActive(), "El negocio está inactivo");
        return value;
    }

    public <T> T local(Class<T> type, Long id, Long businessId, Long branchId,
            Function<T, Business> business, Function<T, Branch> branch) {
        T result = require(type, id);
        check(Objects.equals(business.apply(result).getId(), businessId)
                && Objects.equals(branch.apply(result).getId(), branchId),
                "El registro pertenece a otro negocio o sucursal");
        return result;
    }

    public void businessScope(Business business, Long businessId) {
        check(Objects.equals(business.getId(), businessId), "El registro pertenece a otro negocio");
    }

    public Employee employee(Long businessId, Long branchId, Long employeeId) {
        Employee value = require(Employee.class, employeeId);
        businessScope(value.getBusiness(), businessId);
        check(value.isActive(), "El empleado está inactivo");
        Long assigned = em.createQuery("select count(a) from EmployeeBranchAssignment a "
                + "where a.business.id = :b and a.branch.id = :br and a.employee.id = :id and a.active = true", Long.class)
                .setParameter("b", businessId).setParameter("br", branchId)
                .setParameter("id", employeeId).getSingleResult();
        check(assigned > 0, "El empleado no está asignado a esta sucursal");
        return value;
    }

    public BigDecimal money(BigDecimal value, String field) {
        check(value != null && value.signum() >= 0 && value.stripTrailingZeros().scale() <= 2,
                "Importe inválido: " + field);
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal amount(BigDecimal quantity, BigDecimal unitPrice) {
        check(quantity != null && quantity.signum() > 0 && quantity.stripTrailingZeros().scale() <= 6,
                "Cantidad inválida");
        return quantity.multiply(money(unitPrice, "precio")).setScale(2, RoundingMode.HALF_UP);
    }
}
