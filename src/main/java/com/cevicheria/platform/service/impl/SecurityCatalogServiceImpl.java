package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.SecurityCatalogRequest;
import com.cevicheria.platform.dto.SecurityCatalogResponse;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.model.OperationalFunction;
import com.cevicheria.platform.model.SecurityPermission;
import com.cevicheria.platform.repository.EmployeeAssignmentFunctionRepository;
import com.cevicheria.platform.repository.OperationalFunctionRepository;
import com.cevicheria.platform.repository.SecurityPermissionRepository;
import com.cevicheria.platform.repository.SecurityRolePermissionRepository;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.SecurityCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityCatalogServiceImpl implements SecurityCatalogService {

    private final UsersAccessService access;
    private final SecurityPermissionRepository permissions;
    private final SecurityRolePermissionRepository rolePermissions;
    private final OperationalFunctionRepository functions;
    private final EmployeeAssignmentFunctionRepository assignmentFunctions;

    @Override
    public List<SecurityCatalogResponse> permissions() {
        access.requireSuperAdmin();
        return permissions.findAll(Sort.by("id"))
                .stream().map(this::permissionResponse).toList();
    }
    @Override
    public SecurityCatalogResponse permission(Long id) {
        access.requireSuperAdmin();
        return permissionResponse(permissionEntity(id));
    }
    @Override
    @Transactional
    public SecurityCatalogResponse createPermission(SecurityCatalogRequest request) {
        access.requireSuperAdmin();
        if (request == null || request.code() == null) throw new BadRequestException("Se requiere code");
        String code = request.code().trim();
        if (permissions.existsByCode(code)) throw new ResourceConflictException("Código de permiso duplicado");
        return permissionResponse(permissions.save(new SecurityPermission(code, request.description())));
    }
    @Override
    @Transactional
    public SecurityCatalogResponse updatePermission(Long id, SecurityCatalogRequest request) {
        access.requireSuperAdmin();
        if (request == null) throw new BadRequestException("Datos obligatorios");
        SecurityPermission entity = permissionEntity(id);
        if (request.code() != null && !request.code().trim().equals(entity.getCode())) {
            throw new BadRequestException("El código de un permiso es inmutable");
        }
        if (request.description() != null) entity.changeDescription(request.description());
        return permissionResponse(entity);
    }
    @Override
    @Transactional
    public void deletePermission(Long id) {
        access.requireSuperAdmin();
        SecurityPermission entity = permissionEntity(id);
        if (rolePermissions.existsByIdPermissionId(id)) {
            throw new ResourceConflictException("El permiso está asignado a uno o más roles");
        }
        permissions.delete(entity);
    }
    @Override
    public List<SecurityCatalogResponse> functions() {
        access.requireSuperAdmin();
        return functions.findAll(Sort.by("id"))
                .stream().map(this::functionResponse).toList();
    }
    @Override
    public SecurityCatalogResponse function(Long id) {
        access.requireSuperAdmin();
        return functionResponse(functionEntity(id));
    }
    @Override
    @Transactional
    public SecurityCatalogResponse createFunction(SecurityCatalogRequest request) {
        access.requireSuperAdmin();
        if (request == null || request.code() == null) throw new BadRequestException("Se requiere code");
        String code = request.code().trim();
        if (functions.existsByCode(code)) throw new ResourceConflictException("Código de función duplicado");
        return functionResponse(functions.save(new OperationalFunction(code, request.description())));
    }
    @Override
    @Transactional
    public SecurityCatalogResponse updateFunction(Long id, SecurityCatalogRequest request) {
        access.requireSuperAdmin();
        if (request == null) throw new BadRequestException("Datos obligatorios");
        OperationalFunction entity = functionEntity(id);
        if (request.code() != null && !request.code().trim().equals(entity.getCode())) {
            throw new BadRequestException("El código de una función es inmutable");
        }
        if (request.description() != null) entity.changeDescription(request.description());
        return functionResponse(entity);
    }
    @Override
    @Transactional
    public void deleteFunction(Long id) {
        access.requireSuperAdmin();
        OperationalFunction entity = functionEntity(id);
        if (assignmentFunctions.existsByIdFunctionId(id)) {
            throw new ResourceConflictException("La función está asignada a empleados");
        }
        functions.delete(entity);
    }
    private SecurityPermission permissionEntity(Long id) {
        return permissions.findById(id).orElseThrow(() -> new ResourceNotFoundException("SecurityPermission", "ID", id));
    }
    private OperationalFunction functionEntity(Long id) {
        return functions.findById(id).orElseThrow(() -> new ResourceNotFoundException("OperationalFunction", "ID", id));
    }
    private SecurityCatalogResponse permissionResponse(SecurityPermission p) {
        return new SecurityCatalogResponse(p.getId(), p.getCode(), p.getDescription());
    }
    private SecurityCatalogResponse functionResponse(OperationalFunction p) {
        return new SecurityCatalogResponse(p.getId(), p.getCode(), p.getDescription());
    }
}
