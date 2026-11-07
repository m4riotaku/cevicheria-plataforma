
package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.LoginRequest;
import com.cevicheria.platform.dto.LoginResponse;
import com.cevicheria.platform.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthController
{

    private final AuthService authService;

    @PostMapping("/token")
    public LoginResponse obtenerToken(@RequestBody LoginRequest request)
    {
        return authService.login(request);
    }
}
