package com.ieltsascent.backend.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {
    }

    public record RegisterRequest(
        @NotBlank String fullName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 100) String password
    ) {
    }

    public record LoginRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8, max = 100) String password
    ) {
    }

    public record RefreshRequest(@NotBlank String refreshToken) {
    }

    public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        Long userId,
        String email,
        String fullName,
        String role,
        Double targetBand,
        Double currentBand,
        Boolean onboardingCompleted
    ) {
    }
}
