
package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.security.JwtUtil;
import com.cevicheria.platform.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request)
    {

        var account = repository.findByEmail(request.email().trim()).orElseThrow(this::invalidCredentials);

        if (!account.isActive() || account.getPasswordHash() == null || account.getSessionVersion() == null || !passwordEncoder.matches(request.password(), account.getPasswordHash()))
        {
            throw invalidCredentials();
        }

        String token = jwtUtil.generarToken(account);

        return new LoginResponse(token);
    }

    private ResponseStatusException invalidCredentials()
    {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
    }
}
