package com.cevicheria.platform.specification;

import com.cevicheria.platform.model.UserAccount;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAccountSpecification
{

    public static Specification<UserAccount> byFilter(String userName, String email, Boolean state)
    {
        Specification<UserAccount> spec = Specification.unrestricted();

        if(hasText(userName))
        {
            String pattern = likePattern(userName);

            spec = spec.and(
                    ((root, query, cb) -> cb.like(cb.lower(root.get("userName")),pattern,'\\'))
            );
        }

        if(hasText(email))
        {
            String pattern = likePattern(email);

            spec = spec.and(
                    ((root, query, cb) -> cb.like(cb.lower(root.get("email")),pattern,'\\'))
            );
        }

        if(state != null)
        {
            spec = spec.and(
                    ((root, query, cb) -> cb.equal(root.get("active"),state))
            );
        }

        return spec;
    }

    private static boolean hasText(String value)
    {
        return value != null && !value.isBlank();
    }

    private static String likePattern(String value)
    {
        String valueNormalized = value.trim().toLowerCase(Locale.ROOT);

        String escaped = valueNormalized
                .replace("\\","\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");

        return "%" + escaped + "%";
    }
}
