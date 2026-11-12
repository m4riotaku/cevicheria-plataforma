package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.EmployeeSettlement;

@Repository
public interface EmployeeSettlementRepository extends JpaRepository<EmployeeSettlement,Long>
{
}
