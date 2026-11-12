package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.ComplianceDocument;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplianceDocumentRepository extends JpaRepository<ComplianceDocument, Long>
{
    List<ComplianceDocument> findByBusinessIdOrderByIdAsc(Long businessId);

    Optional<ComplianceDocument> findByIdAndBusinessId(Long id, Long businessId);

    @Query("""
            SELECT d FROM ComplianceDocument d
            LEFT JOIN d.branch branch
            LEFT JOIN d.employee employee
            WHERE d.business.id = :businessId
              AND (branch.id = :branchId OR employee.id IN (
                  SELECT a.employee.id FROM EmployeeBranchAssignment a
                  WHERE a.business.id = :businessId
                    AND a.branch.id = :branchId
                    AND a.active = true
              ))
            ORDER BY d.id
            """)
    List<ComplianceDocument> findVisibleByBranch(
            @Param("businessId") Long businessId,
            @Param("branchId") Long branchId);

    @Query("""
            SELECT d FROM ComplianceDocument d
            LEFT JOIN d.branch branch
            LEFT JOIN d.employee employee
            WHERE d.business.id = :businessId
              AND d.id = :documentId
              AND (branch.id = :branchId OR employee.id IN (
                  SELECT a.employee.id FROM EmployeeBranchAssignment a
                  WHERE a.business.id = :businessId
                    AND a.branch.id = :branchId
                    AND a.active = true
              ))
            """)
    Optional<ComplianceDocument> findVisibleByBranchAndId(
            @Param("businessId") Long businessId,
            @Param("branchId") Long branchId,
            @Param("documentId") Long documentId);
}
