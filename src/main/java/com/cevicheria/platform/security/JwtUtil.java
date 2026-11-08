
package com.cevicheria.platform.security;

import com.cevicheria.platform.model.UserAccount;
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

    private static final Duration EXPIRATION_TIME = Duration.ofDays(30);

    private final Key key;

    public JwtUtil(@Value("${app.jwt.secret}") String secret)
    {
        byte[] decoded = Decoders.BASE64.decode(secret);

        if (decoded.length < 64)
        {
            throw new IllegalArgumentException("JWT_SECRET requiere al menos 64 bytes");
        }

        this.key = Keys.hmacShaKeyFor(decoded);
    }

    public String generarToken(UserAccount account)
    {
        Instant now = Instant.now();

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(account.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(EXPIRATION_TIME)))
                .claim("sv", account.getSessionVersion())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims validarYLeerToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .requireIssuer(ISSUER)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
