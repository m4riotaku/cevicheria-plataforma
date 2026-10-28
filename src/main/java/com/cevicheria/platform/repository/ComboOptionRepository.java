package com.cevicheria.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.ComboOption;

public interface ComboOptionRepository extends JpaRepository<ComboOption, Long> {

	List<ComboOption> findByBusinessId(Long businessId);

	List<ComboOption> findByBusinessIdAndComboProductId(Long businessId, Long comboProductId);

	List<ComboOption> findByBusinessIdAndComboProductIdAndActiveTrue(Long businessId, Long comboProductId);

	List<ComboOption> findByBusinessIdAndOptionProductId(Long businessId, Long optionProductId);
}
