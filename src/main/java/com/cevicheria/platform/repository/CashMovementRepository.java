package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.CashMovement;

@Repository
public interface CashMovementRepository extends JpaRepository<CashMovement,Long>
{
}
