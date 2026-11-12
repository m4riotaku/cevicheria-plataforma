package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.CashRegister;

@Repository
public interface CashRegisterRepository extends JpaRepository<CashRegister,Long>
{
}
