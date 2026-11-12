package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.BusinessUserCreateRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import com.cevicheria.platform.dto.SecurityRoleRequest;
import com.cevicheria.platform.dto.SecurityRoleResponse;
import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.dto.UserBusinessRequest;
import com.cevicheria.platform.dto.UserBusinessResponse;
import com.cevicheria.platform.dto.UserRoleRequest;
import com.cevicheria.platform.dto.UserRoleResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.Branch;
import com.cevicheria.platform.model.Business;
import com.cevicheria.platform.model.Employee;
import com.cevicheria.platform.model.SecurityPermission;
import com.cevicheria.platform.model.SecurityRole;
import com.cevicheria.platform.model.SecurityRolePermission;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.model.UserBusiness;
import com.cevicheria.platform.model.UserRole;
import com.cevicheria.platform.model.id.SecurityRolePermissionId;
import com.cevicheria.platform.repository.BranchRepository;
import com.cevicheria.platform.repository.BusinessRepository;
import com.cevicheria.platform.repository.EmployeeRepository;
import com.cevicheria.platform.repository.SecurityPermissionRepository;
import com.cevicheria.platform.repository.SecurityRolePermissionRepository;
import com.cevicheria.platform.repository.SecurityRoleRepository;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.repository.UserRoleRepository;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.UsersOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersOrganizationServiceImpl implements UsersOrganizationService {

    private final UsersAccessService access;
    private final UserAccountRegistrationService registration;
    private final BusinessRepository businesses;
    private final BranchRepository branches;
    private final UserAccountRepository accounts;
    private final EmployeeRepository employees;
    private final UserBusinessRepository members;
    private final SecurityRoleRepository roles;
    private final UserRoleRepository userRoles;
    private final SecurityPermissionRepository permissions;
    private final SecurityRolePermissionRepository rolePermissions;

    @Override
    @Transactional
    public UserBusinessResponse registerAccount(Long businessId, BusinessUserCreateRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos de cuenta obligatorios");
        if ((request.branchId() == null) != (request.roleId() == null)) {
            throw new BadRequestException("Se requieren branchId y roleId juntos");
        }
        Branch branch = request.branchId() == null ? null : branch(businessId, request.branchId());
        SecurityRole role = request.roleId() == null ? null : roleValue(businessId, request.roleId());
        if (role != null && !role.isActive()) throw new BadRequestException("El rol está inactivo");
        if (branch != null && !branch.isActive()) throw new BadRequestException("La sucursal está inactiva");
        Employee employee = null;
        if (request.employeeId() != null) {
            employee = employees.findByIdAndBusinessId(request.employeeId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", request.employeeId()));
            if (!employee.isActive()) {
                throw new BadRequestException("El empleado está inactivo");
            }
            if (members.existsByBusinessIdAndEmployeeId(businessId, request.employeeId())) {
                throw new ResourceConflictException("El empleado ya tiene una cuenta vinculada al negocio");
            }
        }
        UserAccount account = registration.register(new UserAccountCreateRequest(
                request.userName(), request.email(), request.newPassword()));
        UserBusiness member = new UserBusiness(business(businessId), account);
        if (employee != null) member.changeEmployee(employee);
        members.save(member);
        if (role != null) {
            userRoles.save(new UserRole(business(businessId), branch, member, role));
        }
        return memberResponse(member);
    }

    @Override
    public List<UserBusinessResponse> members(Long businessId) {
        access.requireBusinessOwner(businessId);
        return members.findByBusinessIdOrderByIdAsc(businessId).stream().map(this::memberResponse).toList();
    }

    @Override
    public List<UserBusinessResponse> branchMembers(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
        return members.findActiveByBusinessAndBranch(businessId, branchId)
                .stream().map(this::memberResponse).toList();
    }

    @Override
    public UserBusinessResponse branchMember(Long businessId, Long branchId, Long memberId) {
        access.requireBranchRead(businessId, branchId);
        return members.findActiveByBusinessAndBranch(businessId, branchId).stream()
                .filter(member -> member.getId().equals(memberId))
                .findFirst().map(this::memberResponse)
                .orElseThrow(() -> new ResourceNotFoundException("UserBusiness", "ID", memberId));
    }

    @Override
    public UserBusinessResponse member(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        return memberEntity(businessId, id);
    }

    private UserBusinessResponse memberEntity(Long businessId, Long id) {
        return memberResponse(memberValue(businessId, id));
    }

    @Override
    @Transactional
    public UserBusinessResponse addMember(Long businessId, UserBusinessRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null || request.userId() == null) {
            throw new BadRequestException("Se requiere userId");
        }
        if (members.existsByBusinessIdAndUserAccountId(businessId, request.userId())) {
            throw new ResourceConflictException("La cuenta ya pertenece al negocio");
        }
        UserAccount account = accounts.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", request.userId()));
        if (!account.isActive()) {
            throw new BadRequestException("La cuenta está inactiva");
        }
        UserBusiness member = new UserBusiness(business(businessId), account);
        applyMember(businessId, member, request);
        return memberResponse(members.save(member));
    }

    @Override
    @Transactional
    public UserBusinessResponse changeMember(Long businessId, Long id, UserBusinessRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos obligatorios");
        UserBusiness member = memberValue(businessId, id);
        if (request.userId() != null && !request.userId().equals(member.getUserAccount().getId())) {
            throw new BadRequestException("No se puede cambiar la cuenta del vínculo; crea una nueva asociación");
        }
        checkLastOwner(businessId, member, request.owner(), request.active());
        applyMember(businessId, member, request);
        return memberResponse(member);
    }

    @Override
    @Transactional
    public void deactivateMember(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        UserBusiness member = memberValue(businessId, id);
        checkLastOwner(businessId, member, null, false);
        member.deactivate();
    }

    @Override
    @Transactional
    public void unlinkEmployee(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        memberValue(businessId, id).changeEmployee(null);
    }

    private void checkLastOwner(Long businessId, UserBusiness member, Boolean owner, Boolean active) {
        if (!member.isOwner() || !member.isActive()) return;
        if (Boolean.FALSE.equals(owner) || Boolean.FALSE.equals(active)) {
            if (members.countByBusinessIdAndOwnerTrueAndActiveTrue(businessId) <= 1) {
                throw new ResourceConflictException("No se puede quitar al último propietario activo del negocio");
            }
        }
    }

    private void applyMember(Long businessId, UserBusiness member, UserBusinessRequest request) {
        if (request.employeeId() != null) {
            if (members.existsByBusinessIdAndEmployeeIdAndIdNot(businessId, request.employeeId(),
                    member.getId() == null ? -1L : member.getId())) {
                throw new ResourceConflictException("El empleado ya está vinculado a otra cuenta en el negocio");
            }
            Employee employee = employees.findByIdAndBusinessId(request.employeeId(), businessId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", request.employeeId()));
            if (!employee.isActive()) {
                throw new BadRequestException("El empleado está inactivo");
            }
            member.changeEmployee(employee);
        }
        if (Boolean.TRUE.equals(request.owner())) member.enableOwner();
        if (Boolean.FALSE.equals(request.owner())) member.disableOwner();
        if (Boolean.TRUE.equals(request.active())) member.activate();
        if (Boolean.FALSE.equals(request.active())) member.deactivate();
    }

    @Override
    public List<SecurityRoleResponse> roles(Long businessId) {
        access.requireBusinessOwner(businessId);
        return roles.findByBusinessIdOrderByIdAsc(businessId).stream().map(this::roleResponse).toList();
    }

    @Override
    public SecurityRoleResponse role(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        return roleResponse(roleValue(businessId, id));
    }

    @Override
    @Transactional
    public SecurityRoleResponse createRole(Long businessId, SecurityRoleRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null || request.name() == null) throw new BadRequestException("Se requiere name");
        String name = request.name().trim();
        if (roles.existsByBusinessIdAndName(businessId, name)) {
            throw new ResourceConflictException("Ya existe el rol en el negocio");
        }
        SecurityRole role = new SecurityRole(business(businessId), name);
        if (Boolean.FALSE.equals(request.active())) role.deactivate();
        return roleResponse(roles.save(role));
    }

    @Override
    @Transactional
    public SecurityRoleResponse changeRole(Long businessId, Long id, SecurityRoleRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null) throw new BadRequestException("Datos obligatorios");
        SecurityRole role = roleValue(businessId, id);
        if (request.name() != null) {
            String name = request.name().trim();
            if (roles.existsByBusinessIdAndNameAndIdNot(businessId, name, id)) {
                throw new ResourceConflictException("Ya existe otro rol con el mismo nombre");
            }
            role.changeName(name);
        }
        if (Boolean.TRUE.equals(request.active())) role.activate();
        if (Boolean.FALSE.equals(request.active())) role.deactivate();
        return roleResponse(role);
    }

    @Override
    @Transactional
    public void deactivateRole(Long businessId, Long id) {
        access.requireBusinessOwner(businessId);
        roleValue(businessId, id).deactivate();
    }

    @Override
    public List<UserRoleResponse> userRoles(Long businessId, Long branchId) {
        access.requireBranchRead(businessId, branchId);
        return userRoles.findByBusinessIdAndBranchIdOrderByIdAsc(businessId, branchId)
                .stream().map(this::userRoleResponse).toList();
    }

    @Override
    public UserRoleResponse userRole(Long businessId, Long branchId, Long id) {
        access.requireBranchRead(businessId, branchId);
        UserRole assigned = userRoles.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole", "ID", id));
        if (!assigned.getBranch().getId().equals(branchId)) {
            throw new ResourceNotFoundException("UserRole", "ID", id);
        }
        return userRoleResponse(assigned);
    }

    @Override
    @Transactional
    public UserRoleResponse createUserRole(Long businessId, Long branchId, UserRoleRequest request) {
        // Solo propietario: asignar roles administrativos no debe poder escalarse desde una sucursal.
        access.requireBusinessOwner(businessId);
        if (request == null || request.userBusinessId() == null || request.roleId() == null) {
            throw new BadRequestException("Se requieren userBusinessId y roleId");
        }
        if (userRoles.existsByBusinessIdAndBranchIdAndUserBusinessIdAndRoleId(
                businessId, branchId, request.userBusinessId(), request.roleId())) {
            throw new ResourceConflictException("Rol ya asignado a este usuario y sucursal");
        }
        UserBusiness member = memberValue(businessId, request.userBusinessId());
        SecurityRole role = roleValue(businessId, request.roleId());
        Branch branch = branch(businessId, branchId);
        if (!member.isActive() || !member.getUserAccount().isActive()
                || !role.isActive() || !branch.isActive()) {
            throw new BadRequestException("La cuenta, el vínculo, el rol y la sucursal deben estar activos");
        }
        UserRole assigned = new UserRole(business(businessId), branch, member, role);
        if (Boolean.FALSE.equals(request.active())) assigned.deactivate();
        return userRoleResponse(userRoles.save(assigned));
    }

    @Override
    @Transactional
    public UserRoleResponse changeUserRole(Long businessId, Long branchId, Long id, UserRoleRequest request) {
        access.requireBusinessOwner(businessId);
        if (request == null || request.active() == null) {
            throw new BadRequestException("Se requiere active");
        }
        UserRole assigned = assignedRole(businessId, branchId, id);
        if (request.userBusinessId() != null
                && !request.userBusinessId().equals(assigned.getUserBusiness().getId())) {
            throw new BadRequestException("No puedes cambiar el vínculo; crea otra asignación");
        }
        if (request.roleId() != null && !request.roleId().equals(assigned.getRole().getId())) {
            throw new BadRequestException("No puedes cambiar el rol; crea otra asignación");
        }
        if (request.active()) {
            validateActiveUserRole(assigned);
            assigned.activate();
        } else {
            assigned.deactivate();
        }
        return userRoleResponse(assigned);
    }

    @Override
    @Transactional
    public void deactivateUserRole(Long businessId, Long branchId, Long id) {
        access.requireBusinessOwner(businessId);
        assignedRole(businessId, branchId, id).deactivate();
    }

    @Override
    public List<SecurityCatalogResponse> rolePermissions(Long businessId, Long roleId) {
        access.requireBusinessOwner(businessId);
        roleValue(businessId, roleId);
        return rolePermissions.findByIdBusinessIdAndIdRoleIdOrderByIdPermissionIdAsc(businessId, roleId)
                .stream().map(item -> new SecurityCatalogResponse(item.getPermission().getId(),
                        item.getPermission().getCode(), item.getPermission().getDescription())).toList();
    }

    @Override
    @Transactional
    public void linkPermission(Long businessId, Long roleId, Long permissionId) {
        access.requireBusinessOwner(businessId);
        SecurityRole role = roleValue(businessId, roleId);
        SecurityPermission permission = permissions.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("SecurityPermission", "ID", permissionId));
        SecurityRolePermissionId key = new SecurityRolePermissionId(businessId, roleId, permissionId);
        if (rolePermissions.existsById(key)) return;
        rolePermissions.save(new SecurityRolePermission(business(businessId), role, permission));
    }

    @Override
    @Transactional
    public void unlinkPermission(Long businessId, Long roleId, Long permissionId) {
        access.requireBusinessOwner(businessId);
        roleValue(businessId, roleId);
        SecurityRolePermissionId key = new SecurityRolePermissionId(businessId, roleId, permissionId);
        if (!rolePermissions.existsById(key)) throw new ResourceNotFoundException("SecurityRolePermission", "ID", key);
        rolePermissions.deleteById(key);
    }

    private UserRole assignedRole(Long businessId, Long branchId, Long id) {
        UserRole assigned = userRoles.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("UserRole", "ID", id));
        if (!assigned.getBranch().getId().equals(branchId)) {
            throw new ResourceNotFoundException("UserRole", "ID", id);
        }
        return assigned;
    }

    private void validateActiveUserRole(UserRole assigned) {
        if (!assigned.getUserBusiness().isActive()
                || !assigned.getUserBusiness().getUserAccount().isActive()
                || !assigned.getRole().isActive()
                || !assigned.getBranch().isActive()) {
            throw new BadRequestException("La cuenta, el vínculo, el rol y la sucursal deben estar activos");
        }
    }

    private UserBusiness memberValue(Long businessId, Long id) {
        return members.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("UserBusiness", "ID", id));
    }
    private Business business(Long id) {
        return businesses.findById(id).orElseThrow(() -> new ResourceNotFoundException("Business", "ID", id));
    }
    private Branch branch(Long businessId, Long id) {
        return branches.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "ID", id));
    }
    private SecurityRole roleValue(Long businessId, Long id) {
        return roles.findByIdAndBusinessId(id, businessId)
                .orElseThrow(() -> new ResourceNotFoundException("SecurityRole", "ID", id));
    }
    private UserBusinessResponse memberResponse(UserBusiness u) {
        return new UserBusinessResponse(u.getId(), u.getBusiness().getId(),
                u.getUserAccount().getId(), u.getUserAccount().getUserName(),
                u.getUserAccount().getEmail(), u.getEmployee() == null ? null : u.getEmployee().getId(),
                u.isOwner(), u.isActive());
    }
    private SecurityRoleResponse roleResponse(SecurityRole r) {
        return new SecurityRoleResponse(r.getId(), r.getBusiness().getId(), r.getName(), r.isActive());
    }
    private UserRoleResponse userRoleResponse(UserRole r) {
        return new UserRoleResponse(r.getId(), r.getBusiness().getId(), r.getBranch().getId(),
                r.getUserBusiness().getId(), r.getRole().getId(), r.getRole().getName(), r.isActive());
    }
}
