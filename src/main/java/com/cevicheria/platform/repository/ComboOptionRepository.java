package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.ComboOption;

public interface ComboOptionRepository extends JpaRepository<ComboOption, Long> {

}
