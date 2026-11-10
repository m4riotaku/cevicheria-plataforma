package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.AuthUserResponse;
import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;
import com.cevicheria.platform.dto.RefreshTokenRequest;

public interface AuthService
{
    LoginResponse login(LoginRequest request);
    LoginResponse refresh(RefreshTokenRequest request);
    void logout(RefreshTokenRequest request);
    void logoutAll();
    AuthUserResponse me();
}
