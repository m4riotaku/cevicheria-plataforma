package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.Branch;

public interface BranchRepository extends JpaRepository<Branch,Long>
{
}
