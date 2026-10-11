package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.CashShift;

@Repository
public interface CashShiftRepository extends JpaRepository<CashShift,Long>
{
}
