package com.cevicheria.platform.service.impl;

import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.dto.UserAccountResponse;
import com.cevicheria.platform.dto.UserAccountUpdateRequest;
import com.cevicheria.platform.handlerException.BadRequestException;
import com.cevicheria.platform.handlerException.ResourceConflictException;
import com.cevicheria.platform.handlerException.ResourceNotFoundException;
import com.cevicheria.platform.mapper.UserMapper;
import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.repository.UserAccountRepository;
import com.cevicheria.platform.repository.UserBusinessRepository;
import com.cevicheria.platform.service.UserAccountService;
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
@Transactional(readOnly = true)
public class UserAccountServiceImpl implements UserAccountService
{
    private static final int MIN_PASSWORD_LENGTH = 15;

    private static final int MAX_PASSWORD_BYTES = 72;

    private final UserAccountRepository userRepository;

    private final UserBusinessRepository userBusinessRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserAccountResponse> listAllUsers()
    {
        return searchUsers(null, null, null);
    }

    @Override
    public List<UserAccountResponse> searchUsers(String userName, String email, Boolean active)
    {
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
        validateId(id);

        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));
    }

    @Override
    public UserAccountResponse findUserByEmail(String email)
    {
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
        if(request == null)
        {
            throw new BadRequestException("Los datos del usuario son obligatorios");
        }

        String userNameRequest = normalize(request.userName());
        String emailRequest = normalize(request.email());
        String passwordRequest = request.newPassword();

        validateNewPassword(passwordRequest);

        UserAccount user = new UserAccount(userNameRequest, emailRequest, null);

        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new ResourceConflictException("Ya hay un usuario asociado con este correo");
        }

        if(userRepository.existsByUserName(user.getUserName()))
        {
            throw new ResourceConflictException("Este nombre de usuario ya está en uso");
        }

        user.changePasswordHash(passwordEncoder.encode(passwordRequest));

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserAccountResponse update(Long id, UserAccountUpdateRequest request)
    {
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

        if(userNameRequest == null && emailRequest == null && passwordRequest == null && activeRequest == null)
        {
            throw new BadRequestException("Debes enviar al menos un dato para actualizar");
        }

        if(currentPasswordRequest != null && passwordRequest == null)
        {
            throw new BadRequestException("Debes enviar la contraseña nueva junto con la contraseña actual");
        }

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));

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

            user.changeUserName(userNameRequest);
        }

        if(emailRequest != null)
        {
            if(userRepository.existsByEmailAndIdNot(emailRequest, id))
            {
                throw new ResourceConflictException("El correo electrónico ya está en uso");
            }

            user.changeEmail(emailRequest);
        }

        if(passwordRequest != null)
        {
            user.changePasswordHash(passwordEncoder.encode(passwordRequest));
        }

        if(activeRequest != null)
        {
            if(activeRequest)
            {
                user.activate();
            }
            else
            {
                user.deactivate();
            }
        }

        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id)
    {
        validateId(id);

        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAccount", "ID", id));

        if(userBusinessRepository.existsByUserAccountId(id))
        {
            throw new ResourceConflictException("El usuario está asociado a un negocio. Debes desactivarlo en lugar de eliminarlo");
        }

        userRepository.delete(user);
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
        if(password == null || password.isBlank())
        {
            throw new BadRequestException("La contraseña nueva es obligatoria");
        }

        if(password.codePointCount(0, password.length()) < MIN_PASSWORD_LENGTH)
        {
            throw new BadRequestException("La contraseña nueva debe contener al menos " + MIN_PASSWORD_LENGTH + " caracteres");
        }

        if(password.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES)
        {
            throw new BadRequestException("La contraseña nueva no puede superar 72 bytes UTF-8");
        }
    }
}
