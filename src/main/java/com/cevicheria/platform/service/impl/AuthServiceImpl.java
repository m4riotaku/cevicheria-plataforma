package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.AuthUserResponse;
import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;
import com.cevicheria.platform.handlerException.UnauthenticatedRequestException;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.security.JwtTokenHash;
import com.cevicheria.platform.security.JwtUtil;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UsersAccessService access;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request)
    {
        if (request == null || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().isBlank()) throw unauthorized();

        UserAccount user = users.findByEmail(request.email().trim()).orElseThrow(this::unauthorized);
        if (!user.isActive() || user.getPasswordHash() == null || user.getSessionVersion() == null
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) throw unauthorized();

        String token = jwtUtil.generarToken(user, UUID.randomUUID().toString());
        user.saveAccessTokenHash(JwtTokenHash.hash(token));
        user.changeLastAccessAt(LocalDateTime.now(ZoneOffset.UTC));
        users.save(user);
        return new LoginResponse(token, "Bearer", jwtUtil.expiresInSeconds(user));
    }

    @Override
    @Transactional
    public void logout()
    {
        UserAccount user = users.findById(access.currentUserId()).orElseThrow(this::unauthorized);
        user.invalidateSessions();
        users.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserResponse me()
    {
        UserAccount user = users.findById(access.currentUserId()).orElseThrow(this::unauthorized);
        return new AuthUserResponse(user.getId(), user.getUserName(), user.getEmail(), user.getPlatformRole());
    }

    private UnauthenticatedRequestException unauthorized()
    {
        return new UnauthenticatedRequestException("Credenciales o sesión no válidas");
    }
}
