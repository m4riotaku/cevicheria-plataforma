package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.Employee;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
    List<Employee> findByBusinessIdOrderByIdAsc(Long businessId);

    Optional<Employee> findByIdAndBusinessId(Long id, Long businessId);

    boolean existsByBusinessIdAndDocumentTypeAndDocumentNumber(Long businessId, String documentType, String documentNumber);

    boolean existsByBusinessIdAndDocumentTypeAndDocumentNumberAndIdNot(Long businessId, String documentType, String documentNumber, Long id);
}
