package com.ieltsascent.backend.application.auth;

import java.time.Instant;

public record RefreshTokenClaims(Long userId, String tokenId, Instant expiresAt) {
}
