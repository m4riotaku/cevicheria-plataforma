package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.ComboOption;

public interface ComboOptionRepository extends JpaRepository<ComboOption, Long> {

	List<ComboOption> findByBusiness_Id(Long businessId);

	List<ComboOption> findByBusiness_IdAndComboProduct_Id(Long businessId, Long comboProductId);

	List<ComboOption> findByBusiness_IdAndOptionProduct_Id(Long businessId, Long optionProductId);
}
