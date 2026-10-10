package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.UserBusiness;

@Repository
public interface UserBusinessRepository extends JpaRepository<UserBusiness,Long>
{
}
