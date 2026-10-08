package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business,Long>
{
}
