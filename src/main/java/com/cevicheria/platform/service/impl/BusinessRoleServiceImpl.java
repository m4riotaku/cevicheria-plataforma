package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.BusinessRoleRequest;
import com.cevicheria.platform.dto.BusinessRoleResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.SecurityRole;
import com.cevicheria.platform.model.UserBusiness;
import com.cevicheria.platform.model.UserBusinessRole;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.SecurityRoleRepository;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.repository.UserBusinessRoleRepository;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.BusinessRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BusinessRoleServiceImpl implements BusinessRoleService
{
    private final UsersAccessService access;
    private final BusinessRepository businesses;
    private final UserBusinessRepository members;
    private final SecurityRoleRepository roles;
    private final UserBusinessRoleRepository assignedRoles;

    @Override
    public List<BusinessRoleResponse> list(Long businessId)
    {
        access.requireBusinessOwner(businessId);
        return assignedRoles.findByBusinessIdOrderByIdAsc(businessId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public BusinessRoleResponse assign(Long businessId, BusinessRoleRequest request)
    {
        access.requireBusinessOwner(businessId);

        if (request == null || request.roleId() == null || request.userBusinessId() == null) throw new BadRequestException("Se requieren userBusinessId y roleId");

        UserBusiness member = members.findByIdAndBusinessId(request.userBusinessId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("UserBusiness", "ID", request.userBusinessId()));

        SecurityRole role = roles.findByIdAndBusinessId(request.roleId(), businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SecurityRole", "ID", request.roleId()));

        if (!member.isActive() || !member.getUserAccount().isActive()
                || !member.getBusiness().isActive() || !role.isActive()
                || (member.getEmployee() != null && !member.getEmployee().isActive()))
            throw new BadRequestException("El negocio, la cuenta, el vínculo, el empleado y el rol deben estar activos");

        UserBusinessRole assigned = assignedRoles.findByBusinessIdAndUserBusinessIdAndRoleId(
                businessId, member.getId(), role.getId()).orElse(null);

        if (assigned != null) assigned.activate();

        else
        {
            Business business = businesses.findById(businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Business", "ID", businessId));
            assigned = assignedRoles.save(new UserBusinessRole(business, member, role));
        }

        return toResponse(assigned);
    }

    @Override
    @Transactional
    public void deactivate(Long businessId, Long id)
    {
        access.requireBusinessOwner(businessId);
        UserBusinessRole assigned = assignedRoles.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("UserBusinessRole", "ID", id));
        assigned.deactivate();
    }

    private BusinessRoleResponse toResponse(UserBusinessRole assigned)
    {
        return new BusinessRoleResponse(assigned.getId(), assigned.getBusiness().getId(),
                assigned.getUserBusiness().getId(), assigned.getRole().getId(),
                assigned.getRole().getName(), assigned.isActive());
    }
}
