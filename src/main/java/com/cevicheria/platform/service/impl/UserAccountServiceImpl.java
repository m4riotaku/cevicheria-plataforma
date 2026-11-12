package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.AdminPasswordResetRequest;
import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.dto.UserAccountResponse;
import com.cevicheria.platform.dto.UserAccountUpdateRequest;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.mapper.UserMapper;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.model.enums.PlatformRole;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.service.UserAccountService;
import com.cevicheria.platform.security.UsersAccessService;
import com.cevicheria.platform.security.UserPasswordRules;
import com.cevicheria.platform.specification.UserAccountSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountServiceImpl implements UserAccountService
{
    private static final int MAX_PASSWORD_BYTES = 72;

    private final UsersAccessService usersAccess;

    private final UserAccountRegistrationService registration;

    private final UserAccountRepository userRepository;

    private final UserBusinessRepository userBusinessRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<PlatformRole> listPlatformRoles()
    {
        usersAccess.requireSuperAdmin();
        return List.of(PlatformRole.values());
    }

    @Override
    public List<UserAccountResponse> listAllUsers()
    {
        return searchUsers(null, null, null);
    }

    @Override
    public List<UserAccountResponse> searchUsers(String userName, String email, Boolean active)
    {
        usersAccess.requireSuperAdmin();
        return userRepository.findAll(
                        UserAccountSpecification.byFilter(userName, email, active),
                        Sort.by("id")
                )
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserAccountResponse findUserById(Long id)
    {
        usersAccess.requireSuperAdmin();
        validateId(id);

        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));
    }

    @Override
    public UserAccountResponse findUserByEmail(String email)
    {
        usersAccess.requireSuperAdmin();
        String emailRequest = normalize(email);

        if(emailRequest == null || emailRequest.isBlank())
        {
            throw new BadRequestException("El correo electrónico es obligatorio");
        }

        return userRepository.findByEmail(emailRequest)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "email", emailRequest));
    }

    @Override
    @Transactional
    public UserAccountResponse createUser(UserAccountCreateRequest request)
    {
        usersAccess.requireSuperAdmin();
        UserAccount account = registration.register(request);

        if (request.platformRole() != null)
        {
            account.changePlatformRole(request.platformRole());
        }

        return UserMapper.toDto(account);
    }

    @Override
    @Transactional
    public UserAccountResponse update(Long id, UserAccountUpdateRequest request)
    {
        usersAccess.requireSuperAdmin();
        validateId(id);

        if(request == null)
        {
            throw new BadRequestException("Los datos del usuario son obligatorios");
        }

        String userNameRequest = normalize(request.userName());
        String emailRequest = normalize(request.email());
        String passwordRequest = request.newPassword();
        String currentPasswordRequest = request.currentPassword();
        Boolean activeRequest = request.active();
        PlatformRole roleRequest = request.platformRole();

        if(userNameRequest == null && emailRequest == null && passwordRequest == null
                && activeRequest == null && roleRequest == null)
        {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }

        if(currentPasswordRequest != null && passwordRequest == null)
        {
            throw new BadRequestException("Debes enviar la contraseña nueva junto con la contraseña actual");
        }

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));
        boolean invalidateToken = false;

        if(passwordRequest != null)
        {
            validateNewPassword(passwordRequest);

            if(currentPasswordRequest == null || currentPasswordRequest.isBlank())
            {
                throw new BadRequestException("La contraseña actual es obligatoria para cambiar la contraseña");
            }

            if(currentPasswordRequest.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES)
            {
                throw new BadRequestException("La contraseña actual excede el límite de 72 bytes UTF-8");
            }

            if(!passwordEncoder.matches(currentPasswordRequest, user.getPasswordHash()))
            {
                throw new BadRequestException("La contraseña ingresada no coincide con la actual");
            }
        }

        if(userNameRequest != null)
        {
            if(userRepository.existsByUserNameAndIdNot(userNameRequest, id))
            {
                throw new ResourceConflictException("El nombre de usuario ya está en uso");
            }

            if (!userNameRequest.equals(user.getUserName()))
            {
                user.changeUserName(userNameRequest);
                invalidateToken = true;
            }
        }

        if(emailRequest != null)
        {
            if(userRepository.existsByEmailAndIdNot(emailRequest, id))
            {
                throw new ResourceConflictException("El correo electrónico ya está en uso");
            }

            if (!emailRequest.equals(user.getEmail()))
            {
                user.changeEmail(emailRequest);
                invalidateToken = true;
            }
        }

        if(passwordRequest != null)
        {
            user.changePasswordHash(passwordEncoder.encode(passwordRequest));
            invalidateToken = true;
        }

        if(roleRequest != null && roleRequest != user.getPlatformRole())
        {
            if (id.equals(usersAccess.currentUserId()) && roleRequest != PlatformRole.SUPERADMIN)
            {
                throw new BadRequestException("No puedes quitarte a ti mismo el rol SUPERADMIN");
            }

            user.changePlatformRole(roleRequest);
            invalidateToken = true;
        }

        if(activeRequest != null)
        {
            if(activeRequest)
            {
                user.activate();
            }
            else
            {
                if (id.equals(usersAccess.currentUserId()))
                {
                    throw new BadRequestException("No puedes desactivar tu propia cuenta de administración");
                }
                if (user.isActive())
                {
                    user.deactivate();
                    invalidateToken = true;
                }
            }
        }

        if (invalidateToken)
        {
            user.invalidateSessions();
        }

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id)
    {
        usersAccess.requireSuperAdmin();
        validateId(id);
        if (id.equals(usersAccess.currentUserId()))
        {
            throw new BadRequestException("No puedes eliminar tu propia cuenta de administración");
        }

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));

        if(userBusinessRepository.existsByUserAccountId(id))
        {
            throw new ResourceConflictException("El usuario está asociado a un negocio. Debes desactivarlo en lugar de eliminarlo");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, AdminPasswordResetRequest request) {
        usersAccess.requireSuperAdmin();
        validateId(id);
        if (request == null) {
            throw new BadRequestException("Datos obligatorios");
        }
        String newPassword = request.newPassword();
        UserPasswordRules.validate(newPassword);
        UserAccount account = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));
        account.changePasswordHash(passwordEncoder.encode(newPassword));
        account.invalidateSessions();
    }

    private void validateId(Long id)
    {
        if(id == null || id <= 0)
        {
            throw new BadRequestException("El ID del usuario debe ser un número mayor que cero");
        }
    }

    private String normalize(String value)
    {
        return value == null ? null : value.trim();
    }

    private void validateNewPassword(String password)
    {
        UserPasswordRules.validate(password);
    }
}