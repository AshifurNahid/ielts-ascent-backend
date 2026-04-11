package com.ieltsascent.backend.infrastructure.security;

import com.ieltsascent.backend.application.auth.AccessTokenClaims;
import com.ieltsascent.backend.application.auth.RefreshTokenClaims;
import com.ieltsascent.backend.application.auth.TokenService;
import com.ieltsascent.backend.application.auth.exception.InvalidTokenException;
import com.ieltsascent.backend.domain.auth.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService implements TokenService {
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access-token-ttl}")
    private long accessTokenTtl;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTtl;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_EMAIL, user.getEmail());
        claims.put(CLAIM_ROLES, List.of(user.getRole().name()));
        claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS);
        return buildToken(user.getId(), claims, accessTokenTtl);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH);
        return buildToken(user.getId(), claims, refreshTokenTtl);
    }

    @Override
    public AccessTokenClaims parseAccessToken(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, TOKEN_TYPE_ACCESS);
        Long userId = parseSubject(claims.getSubject());
        String email = claims.get(CLAIM_EMAIL, String.class);
        return new AccessTokenClaims(userId, email, extractRoles(claims));
    }

    @Override
    public RefreshTokenClaims parseRefreshToken(String token) {
        Claims claims = parseClaims(token);
        validateTokenType(claims, TOKEN_TYPE_REFRESH);
        Long userId = parseSubject(claims.getSubject());
        String tokenId = claims.getId();
        if (tokenId == null || tokenId.isBlank()) {
            throw new InvalidTokenException("Missing refresh token id");
        }
        Instant expiresAt = claims.getExpiration().toInstant();
        return new RefreshTokenClaims(userId, tokenId, expiresAt);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException ex) {
            log.debug("JWT token is expired: {}", ex.getMessage());
            throw new InvalidTokenException("Expired token", ex);
        } catch (UnsupportedJwtException ex) {
            log.debug("JWT token is unsupported: {}", ex.getMessage());
            throw new InvalidTokenException("Unsupported token", ex);
        } catch (MalformedJwtException ex) {
            log.debug("JWT token is malformed: {}", ex.getMessage());
            throw new InvalidTokenException("Malformed token", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("JWT validation failed: {}", ex.getMessage());
            throw new InvalidTokenException("Invalid token", ex);
        }
    }

    private void validateTokenType(Claims claims, String expectedType) {
        String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
        if (!expectedType.equals(tokenType)) {
            throw new InvalidTokenException("Invalid token type");
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

    private String buildToken(Long userId, Map<String, Object> claims, long ttlSeconds) {
        Instant now = Instant.now();
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId.toString())
            .setIssuer(issuer)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
            .setId(UUID.randomUUID().toString())
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
