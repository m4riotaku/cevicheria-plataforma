package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SecurityRole;

@Repository
public interface SucurityRoleRepository extends JpaRepository<SecurityRole,Long>
{
}
