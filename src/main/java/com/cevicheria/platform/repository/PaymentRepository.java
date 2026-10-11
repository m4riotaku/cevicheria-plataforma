package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>
{
}
