package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cevicheria.platform.model.BranchProduct;

public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {

}
