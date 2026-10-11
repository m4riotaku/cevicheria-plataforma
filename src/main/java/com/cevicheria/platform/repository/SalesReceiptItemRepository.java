package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.SalesReceiptItem;

@Repository
public interface SalesReceiptItemRepository extends JpaRepository<SalesReceiptItem,Long>
{
}
