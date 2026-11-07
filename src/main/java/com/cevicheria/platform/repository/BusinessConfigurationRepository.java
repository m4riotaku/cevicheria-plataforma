package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BusinessConfiguration;

public interface BusinessConfigurationRepository extends JpaRepository<BusinessConfiguration, Long> {
}
