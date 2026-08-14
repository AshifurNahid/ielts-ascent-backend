package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.domain.auth.Role;

public record AuthTokens(
	String tokenType,
	String accessToken,
	String refreshToken,
	Long userId,
	String email,
	String fullName,
	Role role,
	Double targetBand,
	Double currentBand,
	Boolean onboardingCompleted
) {
}
