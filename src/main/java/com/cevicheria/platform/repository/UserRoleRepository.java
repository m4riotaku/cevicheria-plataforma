package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long>
{
}
