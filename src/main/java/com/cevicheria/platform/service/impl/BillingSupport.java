package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.security.UsersAccessService;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingSupport {
    private final EntityManager em;
    private final UsersAccessService access;

    public <T> T require(Class<T> type, Long id) {
        if (id == null || id <= 0) throw new BadRequestException("El ID debe ser positivo");
        T value = em.find(type, id);
        if (value == null) throw new ResourceNotFoundException(type.getSimpleName(), "ID", id);
        return value;
    }

    public <T> T optional(Class<T> type, Long id) {
        return id == null ? null : require(type, id);
    }

    public Business business(Long id) {
        return require(Business.class, id);
    }

    public Branch branch(Long businessId, Long branchId) {
        Branch value = require(Branch.class, branchId);
        if (!Objects.equals(value.getBusiness().getId(), businessId)) {
            throw new BadRequestException("La sucursal no pertenece al negocio");
        }
        return value;
    }

    public void scope(Business business, Branch branch, Long businessId, Long branchId) {
        if (!Objects.equals(business.getId(), businessId)
                || !Objects.equals(branch.getId(), branchId)) {
            throw new BadRequestException("El registro relacionado pertenece a otro negocio o sucursal");
        }
    }

    public void employeeAtBranch(Employee employee, Long businessId, Long branchId) {
        businessScope(employee.getBusiness(), businessId);
        check(employee.isActive(), "El empleado está inactivo");
        Long assignments = em.createQuery(
                "select count(a) from EmployeeBranchAssignment a "
              + "where a.business.id = :b and a.branch.id = :br "
              + "and a.employee.id = :employee and a.active = true", Long.class)
                .setParameter("b", businessId).setParameter("br", branchId)
                .setParameter("employee", employee.getId()).getSingleResult();
        check(assignments > 0, "El empleado no está asignado a esta sucursal");
    }

    public void businessScope(Business business, Long businessId) {
        if (!Objects.equals(business.getId(), businessId)) {
            throw new BadRequestException("El registro relacionado pertenece a otro negocio");
        }
    }

    public <T> T local(Class<T> type, Long id, Long businessId, Long branchId,
            Function<T, Business> getBusiness, Function<T, Branch> getBranch) {
        T value = require(type, id);
        scope(getBusiness.apply(value), getBranch.apply(value), businessId, branchId);
        return value;
    }

    public <T> List<T> byBranch(Class<T> type, Long businessId, Long branchId) {
        branch(businessId, branchId);
        return em.createQuery("select e from " + type.getSimpleName()
                + " e where e.business.id = :businessId and e.branch.id = :branchId", type)
                .setParameter("businessId", businessId)
                .setParameter("branchId", branchId).getResultList();
    }

    public <T> List<T> byBusiness(Class<T> type, Long businessId) {
        business(businessId);
        return em.createQuery("select e from " + type.getSimpleName()
                + " e where e.business.id = :businessId", type)
                .setParameter("businessId", businessId).getResultList();
    }

    public <T> long count(String jpql, String firstName, Object firstValue,
                          String secondName, Object secondValue) {
        return em.createQuery(jpql, Long.class).setParameter(firstName, firstValue)
                .setParameter(secondName, secondValue).getSingleResult();
    }

    public void read(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
    }

    public void write(Long businessId, Long branchId, String permission) {
        access.requireBranchPermission(businessId, branchId, permission);
    }

    public void businessWrite(Long businessId) {
        access.requireBusinessPermission(businessId, "CONFIGURAR");
    }

    public Long userId() {
        return access.currentUserId();
    }

    public void check(boolean condition, String message) {
        if (!condition) throw new BadRequestException(message);
    }

    public void conflict(boolean condition, String message) {
        if (condition) throw new ResourceConflictException(message);
    }

    public void money(BigDecimal value, String field, boolean positive) {
        if (value == null || (positive ? value.signum() <= 0 : value.signum() < 0)
                || value.stripTrailingZeros().scale() > 2 || value.precision() > 18) {
            throw new BadRequestException("Importe inválido en " + field);
        }
    }

    public void amounts(BigDecimal subtotal, BigDecimal discount, BigDecimal tax,
                        BigDecimal deliveryFee, BigDecimal total) {
        money(subtotal, "subtotal", false);
        money(discount, "discount", false);
        money(tax, "tax", false);
        money(deliveryFee, "deliveryFee", false);
        money(total, "total", false);
        if (discount.compareTo(subtotal) > 0
                || subtotal.subtract(discount).add(tax).add(deliveryFee).compareTo(total) != 0) {
            throw new BadRequestException("El total debe ser subtotal - descuento + impuesto + entrega");
        }
    }

    public void required(Object value, String field) {
        if (value == null) throw new BadRequestException("El campo " + field + " es obligatorio");
    }

    public void requireSameOrder(Long actualId, Long expectedId) {
        if (!Objects.equals(actualId, expectedId)) {
            throw new BadRequestException("El detalle no pertenece al pedido de la cuenta");
        }
    }
}
