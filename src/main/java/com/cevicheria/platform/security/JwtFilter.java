
package com.cevicheria.platform.security;

import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.repository.UserAccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JwtFilter extends OncePerRequestFilter
{

    private final JwtUtil jwtUtil;

    private final UserAccountRepository repository;

    public JwtFilter(JwtUtil jwtUtil, UserAccountRepository repository)
    {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException
    {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer "))
        {

            String token = header.substring(7);

            try
            {
                Claims claims = jwtUtil.validarYLeerToken(token);

                Long userId = Long.valueOf(claims.getSubject());
                Number version = claims.get("sv", Number.class);

                if (version != null)
                {
                    repository.findById(userId)
                            .filter(UserAccount::isActive)
                            .filter(user -> Objects.equals(user.getSessionVersion(), version.intValue()))
                            .ifPresent(user -> {

                                var authentication = new UsernamePasswordAuthenticationToken(user.getId().toString(),null, List.of());

                                var context = SecurityContextHolder.createEmptyContext();

                                context.setAuthentication(authentication);

                                SecurityContextHolder.setContext(context);
                            });
                }

            } catch (JwtException | IllegalArgumentException e)
            {
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}