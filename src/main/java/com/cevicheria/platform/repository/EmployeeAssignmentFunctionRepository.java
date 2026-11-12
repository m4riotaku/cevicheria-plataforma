package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.EmployeeAssignmentFunction;
import com.cevicheria.platform.model.id.EmployeeAssignmentFunctionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeAssignmentFunctionRepository extends JpaRepository<EmployeeAssignmentFunction, EmployeeAssignmentFunctionId>
{
    List<EmployeeAssignmentFunction> findByIdBusinessIdAndIdAssignmentIdOrderByIdFunctionIdAsc(Long businessId, Long assignmentId);

    boolean existsByIdFunctionId(Long functionId);
}