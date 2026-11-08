package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.BusinessUserCreateRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import com.cevicheria.platform.dto.SecurityRoleRequest;
import com.cevicheria.platform.dto.SecurityRoleResponse;
import com.cevicheria.platform.dto.UserBusinessRequest;
import com.cevicheria.platform.dto.UserBusinessResponse;
import com.cevicheria.platform.dto.UserRoleRequest;
import com.cevicheria.platform.dto.UserRoleResponse;
import com.cevicheria.platform.service.UsersOrganizationService;
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
public class UsersOrganizationController {

    private final UsersOrganizationService service;

    @PostMapping("/user-accounts")
    public ResponseEntity<UserBusinessResponse> registerAccount(@PathVariable Long businessId,
                                                        @RequestBody BusinessUserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerAccount(businessId, request));
    }

    @GetMapping("/users")
    public List<UserBusinessResponse> users(@PathVariable Long businessId) {
        return service.members(businessId);
    }
    @GetMapping("/branches/{branchId}/users")
    public List<UserBusinessResponse> branchUsers(@PathVariable Long businessId, @PathVariable Long branchId) {
        return service.branchMembers(businessId, branchId);
    }
    @GetMapping("/branches/{branchId}/users/{id}")
    public UserBusinessResponse branchMember(@PathVariable Long businessId, @PathVariable Long branchId,
                                             @PathVariable Long id) {
        return service.branchMember(businessId, branchId, id);
    }
    @GetMapping("/users/{id}")
    public UserBusinessResponse user(@PathVariable Long businessId, @PathVariable Long id) {
        return service.member(businessId, id);
    }
    @PostMapping("/users")
    public ResponseEntity<UserBusinessResponse> createMember(@PathVariable Long businessId,
                                                  @RequestBody UserBusinessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addMember(businessId, request));
    }
    @PatchMapping("/users/{id}")
    public UserBusinessResponse changeMember(@PathVariable Long businessId, @PathVariable Long id,
                                             @RequestBody UserBusinessRequest request) {
        return service.changeMember(businessId, id, request);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable Long businessId, @PathVariable Long id) {
        service.deactivateMember(businessId, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}/employee")
    public ResponseEntity<Void> unlinkEmployee(@PathVariable Long businessId, @PathVariable Long id) {
        service.unlinkEmployee(businessId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    public List<SecurityRoleResponse> roles(@PathVariable Long businessId) {
        return service.roles(businessId);
    }
    @GetMapping("/roles/{id}")
    public SecurityRoleResponse role(@PathVariable Long businessId, @PathVariable Long id) {
        return service.role(businessId, id);
    }
    @PostMapping("/roles")
    public ResponseEntity<SecurityRoleResponse> createRole(@PathVariable Long businessId,
                                                @RequestBody SecurityRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRole(businessId, request));
    }
    @PatchMapping("/roles/{id}")
    public SecurityRoleResponse changeRole(@PathVariable Long businessId, @PathVariable Long id,
                                            @RequestBody SecurityRoleRequest request) {
        return service.changeRole(businessId, id, request);
    }
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deactivateRole(@PathVariable Long businessId, @PathVariable Long id) {
        service.deactivateRole(businessId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/branches/{branchId}/user-roles")
    public List<UserRoleResponse> userRoles(@PathVariable Long businessId, @PathVariable Long branchId) {
        return service.userRoles(businessId, branchId);
    }
    @GetMapping("/branches/{branchId}/user-roles/{id}")
    public UserRoleResponse userRole(@PathVariable Long businessId, @PathVariable Long branchId,
                                    @PathVariable Long id) {
        return service.userRole(businessId, branchId, id);
    }
    @PostMapping("/branches/{branchId}/user-roles")
    public ResponseEntity<UserRoleResponse> addUserRole(@PathVariable Long businessId,
                         @PathVariable Long branchId, @RequestBody UserRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUserRole(businessId, branchId, request));
    }
    @PatchMapping("/branches/{branchId}/user-roles/{id}")
    public UserRoleResponse changeUserRole(@PathVariable Long businessId, @PathVariable Long branchId,
                  @PathVariable Long id, @RequestBody UserRoleRequest request) {
        return service.changeUserRole(businessId, branchId, id, request);
    }
    @DeleteMapping("/branches/{branchId}/user-roles/{id}")
    public ResponseEntity<Void> removeUserRole(@PathVariable Long businessId, @PathVariable Long branchId,
                                                @PathVariable Long id) {
        service.deactivateUserRole(businessId, branchId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/{roleId}/permissions")
    public List<SecurityCatalogResponse> rolePermissions(@PathVariable Long businessId,
                                                          @PathVariable Long roleId) {
        return service.rolePermissions(businessId, roleId);
    }
    @PutMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> linkPermission(@PathVariable Long businessId, @PathVariable Long roleId,
                                                @PathVariable Long permissionId) {
        service.linkPermission(businessId, roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Void> unlinkPermission(@PathVariable Long businessId, @PathVariable Long roleId,
                                                 @PathVariable Long permissionId) {
        service.unlinkPermission(businessId, roleId, permissionId);
        return ResponseEntity.noContent().build();
    }
}
