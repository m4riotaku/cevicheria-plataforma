package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long>
{
}
