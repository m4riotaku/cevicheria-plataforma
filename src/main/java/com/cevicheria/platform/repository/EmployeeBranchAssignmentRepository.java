package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.EmployeeBranchAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EmployeeBranchAssignmentRepository extends JpaRepository<EmployeeBranchAssignment, Long>
{
    List<EmployeeBranchAssignment> findByBusinessIdOrderByIdAsc(Long businessId);

    Optional<EmployeeBranchAssignment> findByIdAndBusinessId(Long id, Long businessId);

    boolean existsByBusinessIdAndBranchIdAndEmployeeId(Long businessId, Long branchId, Long employeeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select assignment from EmployeeBranchAssignment assignment where assignment.id = :id and assignment.business.id = :businessId")
    Optional<EmployeeBranchAssignment> findForUpdate(@Param("businessId") Long businessId, @Param("id") Long id);

    List<EmployeeBranchAssignment> findByBusinessIdAndBranchIdOrderByIdAsc(
            Long businessId, Long branchId);
}