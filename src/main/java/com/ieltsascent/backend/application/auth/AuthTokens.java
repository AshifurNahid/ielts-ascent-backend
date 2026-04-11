package com.ieltsascent.backend.application.auth;

public record AuthTokens(
	String tokenType,
	String accessToken,
	String refreshToken
) {
}
