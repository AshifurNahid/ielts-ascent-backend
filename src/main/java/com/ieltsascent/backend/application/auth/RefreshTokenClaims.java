package com.ieltsascent.backend.application.auth;

import java.time.Instant;
import java.util.UUID;

public record RefreshTokenClaims(UUID userId, String tokenId, Instant expiresAt) {
}
