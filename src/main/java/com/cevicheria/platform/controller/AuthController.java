package com.cevicheria.platform.controller;

import com.cevicheria.platform.dto.*;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;
    private final UsersAccessService access;

    @PostMapping("/token")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/auth/refresh")
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/logout-all")
    public ResponseEntity<Void> logoutAll() {
        authService.logoutAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth/me")
    public AuthUserResponse me() {
        return authService.me();
    }

    @GetMapping("/auth/access")
    public ResponseEntity<Void> access(@RequestParam Long businessId,
                                       @RequestParam(required = false) Long branchId,
                                       @RequestParam String permission) {
        if (branchId == null) access.requireBusinessPermission(businessId, permission);
        else access.requireBranchPermission(businessId, branchId, permission);
        return ResponseEntity.noContent().build();
    }
}
