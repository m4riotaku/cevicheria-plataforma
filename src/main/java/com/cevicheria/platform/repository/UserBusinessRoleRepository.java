package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.UserBusinessRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserBusinessRoleRepository extends JpaRepository<UserBusinessRole, Long>
{
    List<UserBusinessRole> findByBusinessIdOrderByIdAsc(Long businessId);
    Optional<UserBusinessRole> findByIdAndBusinessId(Long id, Long businessId);
    Optional<UserBusinessRole> findByBusinessIdAndUserBusinessIdAndRoleId(
            Long businessId, Long memberId, Long roleId);

    @Query("""
           SELECT CASE WHEN COUNT(ubr) > 0 THEN true ELSE false END
           FROM UserBusinessRole ubr
           LEFT JOIN ubr.userBusiness.employee employee
           JOIN ubr.role role
           JOIN SecurityRolePermission rp ON rp.role = role
           WHERE ubr.business.id = :businessId
             AND ubr.userBusiness.id = :memberId
             AND ubr.userBusiness.business.id = :businessId
             AND ubr.business.active = true
             AND ubr.userBusiness.active = true
             AND ubr.userBusiness.userAccount.active = true
             AND (employee IS NULL OR employee.active = true)
             AND ubr.active = true
             AND role.active = true
             AND role.business.id = :businessId
             AND rp.business.id = :businessId
             AND rp.permission.code = :permission
           """)
    boolean hasActivePermission(@Param("businessId") Long businessId,
                                @Param("memberId") Long memberId,
                                @Param("permission") String permission);

    @Query("""
           SELECT CASE WHEN COUNT(ubr) > 0 THEN true ELSE false END
           FROM UserBusinessRole ubr
           LEFT JOIN ubr.userBusiness.employee employee
           JOIN ubr.role role
           JOIN SecurityRolePermission rp ON rp.role = role
           WHERE ubr.business.id = :businessId
             AND ubr.userBusiness.id = :memberId
             AND ubr.userBusiness.business.id = :businessId
             AND ubr.business.active = true
             AND ubr.userBusiness.active = true
             AND ubr.userBusiness.userAccount.active = true
             AND (employee IS NULL OR employee.active = true)
             AND ubr.active = true AND role.active = true
             AND role.business.id = :businessId
             AND rp.business.id = :businessId
           """)
    boolean hasAnyActivePermission(@Param("businessId") Long businessId,
                                   @Param("memberId") Long memberId);
}
