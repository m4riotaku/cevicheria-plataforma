package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>
{
    List<UserRole> findByBusinessIdOrderByIdAsc(Long businessId);

    Optional<UserRole> findByIdAndBusinessId(Long id, Long businessId);

    List<UserRole> findByBusinessIdAndBranchIdOrderByIdAsc(Long businessId, Long branchId);

    @Query("""
            SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END
            FROM UserRole ur
            JOIN ur.userBusiness ub
            LEFT JOIN ub.employee employee
            WHERE ur.business.id = :businessId
              AND ur.branch.id = :branchId
              AND ur.userBusiness.id = :membershipId
              AND ur.userBusiness.business.id = :businessId
              AND ur.business.active = true
              AND ur.branch.active = true
              AND ur.userBusiness.userAccount.active = true
              AND (employee IS NULL OR employee.active = true)
              AND ur.userBusiness.active = true
              AND ur.role.business.id = :businessId
              AND ur.role.active = true
              AND ur.active = true
            """)
    boolean existsByBusinessIdAndBranchIdAndUserBusinessIdAndActiveTrue(
            @Param("businessId") Long businessId,
            @Param("branchId") Long branchId,
            @Param("membershipId") Long membershipId);

    @Query("""
            SELECT CASE WHEN COUNT(ur) > 0 THEN true ELSE false END
            FROM UserRole ur
            JOIN ur.userBusiness ub
            LEFT JOIN ub.employee employee
            JOIN ur.role r
            JOIN SecurityRolePermission rp ON rp.role = r
            WHERE ur.business.id = :businessId
              AND ur.branch.id = :branchId
              AND ur.userBusiness.id = :membershipId
              AND ur.userBusiness.business.id = :businessId
              AND ur.business.active = true
              AND ur.branch.active = true
              AND ur.userBusiness.userAccount.active = true
              AND (employee IS NULL OR employee.active = true)
              AND ur.userBusiness.active = true
              AND ur.active = true
              AND r.business.id = :businessId
              AND r.active = true
              AND rp.business.id = :businessId
              AND rp.permission.code = :permissionCode
            """)
    boolean hasActivePermission(
            @Param("businessId") Long businessId,
            @Param("branchId") Long branchId,
            @Param("membershipId") Long membershipId,
            @Param("permissionCode") String permissionCode);

    boolean existsByBusinessIdAndBranchIdAndUserBusinessIdAndRoleId(
            Long businessId, Long branchId, Long userBusinessId, Long roleId);
}
