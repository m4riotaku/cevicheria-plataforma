package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.BusinessUserCreateRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import com.cevicheria.platform.dto.SecurityRoleRequest;
import com.cevicheria.platform.dto.SecurityRoleResponse;
import com.cevicheria.platform.dto.UserBusinessRequest;
import com.cevicheria.platform.dto.UserBusinessResponse;
import com.cevicheria.platform.dto.UserRoleRequest;
import com.cevicheria.platform.dto.UserRoleResponse;
import java.util.List;

public interface UsersOrganizationService {
    UserBusinessResponse registerAccount(Long businessId, BusinessUserCreateRequest request);
    List<UserBusinessResponse> members(Long businessId);
    List<UserBusinessResponse> branchMembers(Long businessId, Long branchId);
    UserBusinessResponse branchMember(Long businessId, Long branchId, Long memberId);
    UserBusinessResponse member(Long businessId, Long id);
    UserBusinessResponse addMember(Long businessId, UserBusinessRequest request);
    UserBusinessResponse changeMember(Long businessId, Long id, UserBusinessRequest request);
    void deactivateMember(Long businessId, Long id);
    void unlinkEmployee(Long businessId, Long id);

    List<SecurityRoleResponse> roles(Long businessId);
    SecurityRoleResponse role(Long businessId, Long id);
    SecurityRoleResponse createRole(Long businessId, SecurityRoleRequest request);
    SecurityRoleResponse changeRole(Long businessId, Long id, SecurityRoleRequest request);
    void deactivateRole(Long businessId, Long id);

    List<UserRoleResponse> userRoles(Long businessId, Long branchId);
    UserRoleResponse userRole(Long businessId, Long branchId, Long userRoleId);
    UserRoleResponse createUserRole(Long businessId, Long branchId, UserRoleRequest request);
    UserRoleResponse changeUserRole(Long businessId, Long branchId, Long id, UserRoleRequest request);
    void deactivateUserRole(Long businessId, Long branchId, Long id);

    List<SecurityCatalogResponse> rolePermissions(Long businessId, Long roleId);
    void linkPermission(Long businessId, Long roleId, Long permissionId);
    void unlinkPermission(Long businessId, Long roleId, Long permissionId);
}
