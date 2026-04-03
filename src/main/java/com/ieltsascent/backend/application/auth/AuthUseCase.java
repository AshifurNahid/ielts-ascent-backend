package com.ieltsascent.backend.application.auth;

public interface AuthUseCase {
    AuthTokens register(String fullName, String email, String password);

    AuthTokens login(String email, String password);

    AuthTokens refresh(String refreshToken);
}
