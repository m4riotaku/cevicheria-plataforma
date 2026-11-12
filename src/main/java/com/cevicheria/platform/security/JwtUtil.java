package com.cevicheria.platform.security;

import com.cevicheria.platform.model.UserAccount;
import com.cevicheria.platform.model.enums.PlatformRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil
{
    private static final String ISSUER = "cevicheria-api";
    private static final String AUDIENCE = "cevicheria-api";
    private final Key key;
    private final Duration accessTtl;
    private final Duration adminTtl;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.access-minutes:30}") long accessMinutes,
                   @Value("${app.jwt.superadmin-access-minutes:30}") long adminMinutes)
    {
        byte[] decoded = Decoders.BASE64.decode(secret);
        if (decoded.length < 64) throw new IllegalArgumentException("JWT_SECRET requiere 64 bytes aleatorios en Base64");
        if (accessMinutes < 1 || adminMinutes < 1) throw new IllegalArgumentException("Duración JWT inválida");
        key = Keys.hmacShaKeyFor(decoded);
        accessTtl = Duration.ofMinutes(accessMinutes);
        adminTtl = Duration.ofMinutes(adminMinutes);
    }

    public long expiresInSeconds(UserAccount account)
    {
        return ttl(account).toSeconds();
    }

    private Duration ttl(UserAccount account)
    {
        return account.getPlatformRole() == PlatformRole.SUPERADMIN ? adminTtl : accessTtl;
    }

    public String generarToken(UserAccount account, String sessionId)
    {
        Instant now = Instant.now();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setAudience(AUDIENCE)
                .setSubject(account.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(ttl(account))))
                .claim("sv", account.getSessionVersion())
                .claim("sid", sessionId)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims validarYLeerToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(ISSUER)
                .requireAudience(AUDIENCE)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}