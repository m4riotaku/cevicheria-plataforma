package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.ComplianceDocument;

@Repository
public interface ComplianceDocumentRepository extends JpaRepository<ComplianceDocument, Long>
{
}
