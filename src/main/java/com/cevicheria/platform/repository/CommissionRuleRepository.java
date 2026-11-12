package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.CommissionRule;

@Repository
public interface CommissionRuleRepository extends JpaRepository<CommissionRule,Long>
{
}
