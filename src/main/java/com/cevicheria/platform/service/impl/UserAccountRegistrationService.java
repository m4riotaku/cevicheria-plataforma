package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.security.UserPasswordRules;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAccountRegistrationService
{
    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount register(UserAccountCreateRequest request)
    {
        if(request == null)
        {
            throw new BadRequestException("Los datos del usuario son obligatorios");
        }

        String userName = request.userName() == null ? null : request.userName().trim();
        String email = request.email() == null ? null : request.email().trim();
        String password = request.newPassword();

        UserPasswordRules.validate(password);

        UserAccount account = new UserAccount(userName, email, null);

        if(repository.existsByEmail(account.getEmail()))
        {
            throw new ResourceConflictException("El correo electrónico ya está registrado");
        }

        if(repository.existsByUserName(account.getUserName()))
        {
            throw new ResourceConflictException("El nombre de usuario ya está registrado");
        }

        account.changePasswordHash(passwordEncoder.encode(password));

        // El rol global por defecto es USER. Solo el CRUD de superadministrador
        // puede cambiarlo; el registro desde negocio nunca lo eleva.
        return repository.save(account);
    }
}
