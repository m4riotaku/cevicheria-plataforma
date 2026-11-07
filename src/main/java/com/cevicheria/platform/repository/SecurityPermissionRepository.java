package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.SecurityPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityPermissionRepository extends JpaRepository<SecurityPermission, Long>
{
    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
