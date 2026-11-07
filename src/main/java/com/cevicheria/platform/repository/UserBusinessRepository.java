package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.UserBusiness;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBusinessRepository extends JpaRepository<UserBusiness,Long>
{
    List<UserBusiness> findByBusinessIdOrderByIdAsc(Long businessId);

    List<UserBusiness> findByUserAccountIdOrderByIdAsc(Long userId);

    Optional<UserBusiness> findByIdAndBusinessId(Long id, Long businessId);

    boolean existsByUserAccountId(Long id);

    long countByBusinessIdAndOwnerTrueAndActiveTrue(Long businessId);

    Optional<UserBusiness> findByBusinessIdAndUserAccountId(Long businessId, Long userId);

    @Query("""
        SELECT DISTINCT ub FROM UserRole ur
        JOIN ur.userBusiness ub
        LEFT JOIN ub.employee employee
        WHERE ur.business.id = :businessId
          AND ub.business.id = :businessId
          AND ur.branch.id = :branchId
          AND ur.branch.active = true
          AND ub.business.active = true
          AND ub.active = true
          AND ub.userAccount.active = true
          AND ur.active = true
          AND ur.role.active = true
          AND (employee IS NULL OR employee.active = true)
        ORDER BY ub.id
        """)
    List<UserBusiness> findActiveByBusinessAndBranch(
            @Param("businessId") Long businessId,
            @Param("branchId") Long branchId);


    boolean existsByBusinessIdAndUserAccountId(Long businessId, Long userId);

    boolean existsByBusinessIdAndEmployeeId(Long businessId, Long employeeId);

    boolean existsByBusinessIdAndEmployeeIdAndIdNot(Long businessId, Long employeeId, Long id);
}
