package com.ieltsascent.backend.infrastructure.security;

import com.ieltsascent.backend.application.auth.AccessTokenClaims;
import com.ieltsascent.backend.application.auth.RefreshTokenClaims;
import com.ieltsascent.backend.application.auth.TokenService;
import com.ieltsascent.backend.application.auth.exception.InvalidTokenException;
import com.ieltsascent.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements TokenService {
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    private final JwtProperties properties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        return generateToken(user, properties.accessTokenTtl(), TOKEN_TYPE_ACCESS);
    }

    @Override
    public String generateRefreshToken(User user) {
        return generateToken(user, properties.refreshTokenTtl(), TOKEN_TYPE_REFRESH);
    }

    @Override
    public AccessTokenClaims parseAccessToken(String token) {
        Claims claims = parseToken(token, TOKEN_TYPE_ACCESS);
        Long userId = parseSubject(claims.getSubject());
        String email = claims.get(CLAIM_EMAIL, String.class);
        return new AccessTokenClaims(userId, email, extractRoles(claims));
    }

    @Override
    public RefreshTokenClaims parseRefreshToken(String token) {
        Claims claims = parseToken(token, TOKEN_TYPE_REFRESH);
        Long userId = parseSubject(claims.getSubject());
        String tokenId = claims.getId();
        if (tokenId == null || tokenId.isBlank()) {
            throw new InvalidTokenException("Missing refresh token id");
        }
        Instant expiresAt = claims.getExpiration().toInstant();
        return new RefreshTokenClaims(userId, tokenId, expiresAt);
    }

    private Claims parseToken(String token, String expectedType) {
        try {
            Claims claims = Jwts.parserBuilder()
                .requireIssuer(properties.issuer())
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
            String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
            if (!expectedType.equals(tokenType)) {
                throw new InvalidTokenException("Invalid token type");
            }
            return claims;
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid token", ex);
        }
    }

    private Long parseSubject(String subject) {
        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException ex) {
            throw new InvalidTokenException("Invalid token subject", ex);
        }
    }

    private List<String> extractRoles(Claims claims) {
        Object roles = claims.get(CLAIM_ROLES);
        if (roles instanceof List<?> roleList) {
            return roleList.stream()
                .filter(value -> value != null && !value.toString().isBlank())
                .map(Object::toString)
                .toList();
        }
        String role = claims.get("role", String.class);
        if (role != null && !role.isBlank()) {
            return List.of(role);
        }
        return List.of();
    }

    private String generateToken(User user, long ttlSeconds, String type) {
        Instant now = Instant.now();
        var builder = Jwts.builder()
            .setSubject(user.getId().toString())
            .setIssuer(properties.issuer())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
            .setId(UUID.randomUUID().toString())
            .claim(CLAIM_TOKEN_TYPE, type);
        if (TOKEN_TYPE_ACCESS.equals(type)) {
            builder
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLES, List.of(user.getRole().name()));
        }
        return builder.signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }
}
