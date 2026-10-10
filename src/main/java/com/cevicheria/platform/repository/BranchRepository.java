package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch,Long>
{
}
