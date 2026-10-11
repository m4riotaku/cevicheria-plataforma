package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.OrderBill;

@Repository
public interface OrderBillRepository extends JpaRepository<OrderBill,Long>
{
}
