package com.aram.erpcrud.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtHandler {

    private final String applicationName;
    private final String accessTokensBase64Secret;
    private final long validityInMilliseconds;

    public JwtHandler(
            @Value("${spring.application.name}") String applicationName,
            @Value("${jwt.access-tokens-base-64-secret}") String accessTokensBase64Secret,
            @Value("${jwt.validity-milliseconds}") long validityInMilliseconds
    ) {
        this.applicationName = applicationName;
        this.accessTokensBase64Secret = accessTokensBase64Secret;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                .issuer(applicationName)
                .subject(username)
                .audience().add(applicationName).and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(getSigningKey())
                .compact();
    }

    public Optional<String> parseUsername(String token) {
        try {
             Claims claims = parseClaims(token);
             return Optional.ofNullable(claims.getSubject());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public boolean validateToken(String token) {
        try {

            Claims claims = parseClaims(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            if (!claims.getIssuer().equals(applicationName)) {
                return false;
            }

            Set<String> audiences = claims.getAudience();
            return audiences.contains(applicationName);

        } catch (Exception ex) {
            return false;
        }
    }

    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader == null) {
            return null;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7);
    }

    private Claims parseClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokensBase64Secret));
    }
}