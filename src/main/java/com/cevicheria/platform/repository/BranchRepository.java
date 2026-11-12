package com.cevicheria.platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Branch;

public interface BranchRepository extends JpaRepository<Branch, Long>
{
    List<Branch> findByBusinessId(Long businessId);
    boolean existsByIdAndBusinessId(Long id, Long businessId);
    boolean existsByIdAndBusinessIdAndActiveTrue(Long id, Long businessId);
    List<Branch> findByBusinessIdAndActiveTrue(Long businessId);
    Optional<Branch> findByIdAndBusinessId(Long id, Long businessId);
    Optional<Branch> findByBusinessIdAndName(Long businessId, String name);
    Optional<Branch> findByMenuSlug(String menuSlug);
}
