package com.ieltsascent.backend.api.auth;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthTokens tokens = authService.register(request.fullName(), request.email(), request.password());
        return ApiResponseUtil.success(toResponse(tokens), ApiResponseConstant.USER_REGISTERED, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthTokens tokens = authService.login(request.email(), request.password());
        return ApiResponseUtil.success(toResponse(tokens), ApiResponseConstant.LOGIN_SUCCESS);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthTokens tokens = authService.refresh(request.refreshToken());
        return ApiResponseUtil.success(toResponse(tokens), ApiResponseConstant.TOKEN_REFRESHED);
    }

    private static AuthResponse toResponse(AuthTokens tokens) {
        return new AuthResponse(
            tokens.tokenType(),
            tokens.accessToken(),
            tokens.refreshToken(),
            tokens.userId(),
            tokens.email(),
            tokens.fullName(),
            tokens.role().name(),
            tokens.targetBand(),
            tokens.currentBand(),
            tokens.onboardingCompleted()
        );
    }
}
