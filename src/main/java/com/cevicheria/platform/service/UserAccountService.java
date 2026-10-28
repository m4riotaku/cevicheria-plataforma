package com.cevicheria.platform.service;

import com.cevicheria.platform.dto.UserAccountCreateRequest;
import com.cevicheria.platform.dto.UserAccountResponse;
import com.cevicheria.platform.dto.UserAccountUpdateRequest;
import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserAccountService
{
    List<UserAccountResponse> listAllUsers();

    List<UserAccountResponse> searchUsers(String userName, String email, Boolean active);

    UserAccountResponse findUserById(Long idUser);

    UserAccountResponse findUserByEmail(String email);

    UserAccountResponse update(Long id, UserAccountUpdateRequest request);

    UserAccountResponse createUser(UserAccountCreateRequest request);

    void deleteUser(Long id);

}
