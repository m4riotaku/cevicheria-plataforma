package com.cevicheria.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.cevicheria.platform.model.UserAccount;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> , JpaSpecificationExecutor<UserAccount>
{

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findUserById(Long id);

    boolean existsById(Long id);

    boolean existsByUserNameAndIdNot(String userName, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
