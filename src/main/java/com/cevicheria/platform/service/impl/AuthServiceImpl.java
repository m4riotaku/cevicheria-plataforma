package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.AuthUserResponse;
import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;
import com.cevicheria.platform.dto.RefreshTokenRequest;
import com.cevicheria.platform.handlerException.UnauthenticatedRequestException;
import com.cevicheria.platform.model.AuthSession;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.repository.AuthSessionRepository;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.security.JwtUtil;
import com.cevicheria.platform.security.RefreshTokenCodec;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private final UserAccountRepository users;
    private final AuthSessionRepository sessions;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenCodec refreshCodec;
    private final UsersAccessService access;

    @Value("${app.jwt.refresh-days:7}")
    private long refreshDays;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request)
    {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) throw unauthorized();

        UserAccount user = users.findByEmail(request.email().trim()).orElseThrow(this::unauthorized);
        if (!user.isActive() || user.getPasswordHash() == null || user.getSessionVersion() == null
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) throw unauthorized();

        if (refreshDays < 1) throw new IllegalStateException("app.jwt.refresh-days debe ser positivo");

        String sid = UUID.randomUUID().toString();
        String refreshToken = refreshCodec.generate(sid);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        sessions.save(new AuthSession(sid, user, refreshCodec.hash(refreshToken), now, now.plusDays(refreshDays)));
        return tokens(user, sid, refreshToken);
    }

    @Override
    @Transactional(noRollbackFor = UnauthenticatedRequestException.class)
    public LoginResponse refresh(RefreshTokenRequest request)
    {
        String token = tokenFrom(request);
        String sid = refreshCodec.sessionId(token);
        if (sid == null) throw unauthorized();
        AuthSession session = sessions.findForUpdate(sid).orElseThrow(this::unauthorized);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        if (!session.isUsable(now)) throw unauthorized();

        if (!refreshCodec.matches(token, session.getRefreshTokenHash()))
        {
            session.revoke(now);
            sessions.flush();
            throw unauthorized();
        }

        UserAccount user = session.getUserAccount();
        if (!user.isActive() || user.getSessionVersion() == null
                || !user.getSessionVersion().equals(session.getSessionVersion()))

        {
            session.revoke(now);
            sessions.flush();
            throw unauthorized();
        }

        String rotated = refreshCodec.generate(sid);
        session.rotate(refreshCodec.hash(rotated), now);
        return tokens(user, sid, rotated);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequest request)
    {
        String token = tokenFrom(request);
        String sid = refreshCodec.sessionId(token);
        if (sid == null) throw unauthorized();
        AuthSession session = sessions.findForUpdate(sid).orElseThrow(this::unauthorized);
        if (!session.isUsable(LocalDateTime.now(ZoneOffset.UTC))
                || !refreshCodec.matches(token, session.getRefreshTokenHash())) throw unauthorized();
        session.revoke(LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    @Transactional
    public void logoutAll()
    {
        UserAccount user = users.findById(access.currentUserId()).orElseThrow(this::unauthorized);
        user.invalidateSessions();
        sessions.revokeAllForUser(user.getId(), LocalDateTime.now(ZoneOffset.UTC));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse me()
    {
        UserAccount user = users.findById(access.currentUserId()).orElseThrow(this::unauthorized);
        return new AuthUserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getPlatformRole());
    }

    private LoginResponse tokens(UserAccount user, String sid, String refresh)
    {
        return new LoginResponse(jwtUtil.generarToken(user, sid), refresh, "Bearer", jwtUtil.expiresInSeconds(user));
    }

    private String tokenFrom(RefreshTokenRequest request)
    {
        if (request == null || request.refreshToken() == null) throw unauthorized();
        return request.refreshToken();
    }

    private UnauthenticatedRequestException unauthorized()
    {
        return new UnauthenticatedRequestException("Credenciales o sesión no válidas");
    }
}
