package com.cevicheria.platform.mapper;

import com.cevicheria.platform.dto.UserAccountResponse;
import com.cevicheria.platform.model.UserAccount;

public class UserMapper
{
    public static UserAccountResponse toDto(UserAccount userAccount)
    {
        return new UserAccountResponse(
                userAccount.getId(),
                userAccount.getUserName(),
                userAccount.getEmail(),
                userAccount.isActive(),
                userAccount.getCreatedAt()
        );
    }
}
