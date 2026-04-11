package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.domain.auth.User;

public interface TokenService {
    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    AccessTokenClaims parseAccessToken(String token);

    RefreshTokenClaims parseRefreshToken(String token);
}
