package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
}
