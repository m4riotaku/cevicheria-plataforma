package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.EmployeeIncentive;

@Repository
public interface EmployeeIncentiveRepository extends JpaRepository<EmployeeIncentive,Long>
{
}
