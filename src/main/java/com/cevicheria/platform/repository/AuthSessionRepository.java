package com.cevicheria.platform.repository;

import com.cevicheria.platform.model.AuthSession;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthSessionRepository extends JpaRepository<AuthSession, Long>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from AuthSession s join fetch s.userAccount where s.sessionId = :sid")
    Optional<AuthSession> findForUpdate(@Param("sid") String sessionId);

    boolean existsByUserAccountId(Long userId);

    boolean existsBySessionIdAndUserAccountIdAndRevokedAtIsNullAndExpiresAtAfter(String sessionId, Long userId, LocalDateTime now);

    @Modifying
    @Query("update AuthSession s set s.revokedAt = :now where s.userAccount.id = :userId and s.revokedAt is null")
    int revokeAllForUser(@Param("userId") Long userId, @Param("now") LocalDateTime now);
}
