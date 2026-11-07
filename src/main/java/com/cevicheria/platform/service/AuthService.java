package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;

public interface AuthService
{
    LoginResponse login(LoginRequest request);
}
