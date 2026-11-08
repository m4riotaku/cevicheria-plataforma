package com.cevicheria.platform.security;

import com.cevicheria.platform.handlerException.ForbiddenOperationException;
import com.cevicheria.platform.handlerException.UnauthenticatedRequestException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.UserBusiness;
import com.cevicheria.platform.model.enums.PlatformRole;

import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.repository.UserRoleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsersAccessService
{

    private final UserBusinessRepository userBusinesses;
    private final UserRoleRepository userRoles;
    private final BranchRepository branches;
    private final UserAccountRepository accounts;

    public UsersAccessService(UserBusinessRepository userBusinesses, UserRoleRepository userRoles, BranchRepository branches, UserAccountRepository accounts)
    {
        this.userBusinesses = userBusinesses;
        this.userRoles = userRoles;
        this.branches = branches;
        this.accounts = accounts;
    }

    public Long currentUserId()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof String value))
        {
            throw new UnauthenticatedRequestException("Debes iniciar sesión");
        }

        try
        {
            return Long.valueOf(value);
        } catch (NumberFormatException ex)
        {
            throw new UnauthenticatedRequestException("Identidad inválida");
        }
    }

    public boolean isSuperAdmin()
    {
        Long userId = currentUserId();
        return accounts.findById(userId)
                .map(user -> user.isActive() && user.getPlatformRole() == PlatformRole.SUPERADMIN)
                .orElse(false);
    }

    public void requireSuperAdmin()
    {
        if (!isSuperAdmin())
        {
            forbidden();
        }
    }

    public boolean isBusinessOwner(Long businessId)
    {
        if (isSuperAdmin()) return true;
        return membership(businessId)
                .map(m -> m.isActive() && m.isOwner()
                        && m.getBusiness().isActive()
                        && m.getUserAccount().isActive()
                        && (m.getEmployee() == null || m.getEmployee().isActive()))
                .orElse(false);
    }

    public void requireBusinessOwner(Long businessId)
    {
        if (!isBusinessOwner(businessId)) forbidden();
    }

    public void requireBranchRead(Long businessId, Long branchId)
    {
        ensureBranch(businessId, branchId);
        if (isBusinessOwner(businessId)) return;
        UserBusiness member = membership(businessId)
                .filter(UserBusiness::isActive)
                .orElseThrow(this::forbiddenException);
        if (userRoles.existsByBusinessIdAndBranchIdAndUserBusinessIdAndActiveTrue(businessId, branchId, member.getId())) return;
        forbidden();
    }

    public void requireBranchManage(Long businessId, Long branchId)
    {
        ensureBranch(businessId, branchId);
        if (isBusinessOwner(businessId)) return;
        UserBusiness member = membership(businessId)
                .filter(UserBusiness::isActive)
                .orElseThrow(this::forbiddenException);
        if (userRoles.hasActivePermission(
                businessId, branchId, member.getId(), "CONFIGURAR")) return;
        forbidden();
    }

    private Optional<UserBusiness> membership(Long businessId)
    {
        return userBusinesses.findByBusinessIdAndUserAccountId(businessId, currentUserId());
    }

    private void ensureBranch(Long businessId, Long branchId)
    {
        if (!branches.existsByIdAndBusinessId(branchId, businessId))
        {
            throw new ResourceNotFoundException(
                    "Branch", "ID", branchId);
        }
    }

    private ForbiddenOperationException forbiddenException()
    {
        return new ForbiddenOperationException("No tienes permisos para esta operación");
    }

    private void forbidden()
    {
        throw forbiddenException();
    }
}
