package com.ieltsascent.backend.api.auth;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.auth.dto.AuthDtos.AuthResponse;
import com.ieltsascent.backend.api.auth.dto.AuthDtos.LoginRequest;
import com.ieltsascent.backend.api.auth.dto.AuthDtos.RefreshRequest;
import com.ieltsascent.backend.api.auth.dto.AuthDtos.RegisterRequest;
import com.ieltsascent.backend.application.auth.AuthTokens;
import com.ieltsascent.backend.application.auth.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthTokens tokens = authService.register(request.fullName(), request.email(), request.password());
        return ApiResponse.success(toResponse(tokens));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthTokens tokens = authService.login(request.email(), request.password());
        return ApiResponse.success(toResponse(tokens));
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthTokens tokens = authService.refresh(request.refreshToken());
        return ApiResponse.success(toResponse(tokens));
    }

    private static AuthResponse toResponse(AuthTokens tokens) {
        return new AuthResponse(
            tokens.tokenType(),
            tokens.accessToken(),
            tokens.refreshToken()
        );
    }
}
