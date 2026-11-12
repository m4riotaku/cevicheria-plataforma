package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.SecurityRolePermission;
import com.cevicheria.platform.model.id.SecurityRolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SecurityRolePermissionRepository extends JpaRepository<SecurityRolePermission, SecurityRolePermissionId>
{
    List<SecurityRolePermission> findByIdBusinessIdAndIdRoleIdOrderByIdPermissionIdAsc(Long businessId, Long roleId);

    boolean existsByIdPermissionId(Long permissionId);
}