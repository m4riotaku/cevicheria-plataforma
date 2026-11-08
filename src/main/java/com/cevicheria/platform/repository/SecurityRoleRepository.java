package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SecurityRole;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRoleRepository extends JpaRepository<SecurityRole,Long>
{
    List<SecurityRole> findByBusinessIdOrderByIdAsc(Long businessId);

    Optional<SecurityRole> findByIdAndBusinessId(Long id, Long businessId);

    boolean existsByBusinessIdAndName(Long businessId, String name);

    boolean existsByBusinessIdAndNameAndIdNot(Long businessId, String name, Long id);
}
