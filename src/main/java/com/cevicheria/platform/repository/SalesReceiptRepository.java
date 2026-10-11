package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SalesReceipt;

@Repository
public interface SalesReceiptRepository extends JpaRepository<SalesReceipt,Long>
{
}
