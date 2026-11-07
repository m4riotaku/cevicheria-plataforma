package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BranchConfiguration;
import com.cevicheria.platform.model.id.BranchConfigurationId;

public interface BranchConfigurationRepository extends JpaRepository<BranchConfiguration, BranchConfigurationId> {
    List<BranchConfiguration> findByIdBusinessId(Long businessId);
}
