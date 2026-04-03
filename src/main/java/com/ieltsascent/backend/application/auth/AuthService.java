package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.application.auth.exception.InvalidCredentialsException;
import com.ieltsascent.backend.application.auth.exception.InvalidTokenException;
import com.ieltsascent.backend.application.auth.exception.UserAlreadyExistsException;
import com.ieltsascent.backend.application.auth.exception.UserNotFoundException;
import com.ieltsascent.backend.domain.auth.RefreshToken;
import com.ieltsascent.backend.domain.auth.Role;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.auth.UserProfile;
import com.ieltsascent.backend.infrastructure.persistence.RefreshTokenRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements AuthUseCase {
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    @Transactional
    public AuthTokens register(String fullName, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.info("Registration rejected: email already registered");
            throw new UserAlreadyExistsException();
        }
        UserProfile profile = new UserProfile();
        profile.setTargetBand(7.0);
        profile.setExamDate(LocalDate.now().plusMonths(3));
        profile.setTimezone("UTC");
        profileRepository.save(profile);

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.STUDENT);
        user.setProfile(profile);
        userRepository.save(user);

        log.info("User registered id={}", user.getId());
        AuthTokens tokens = new AuthTokens(
            tokenService.generateAccessToken(user),
            tokenService.generateRefreshToken(user)
        );
        storeRefreshToken(tokens.refreshToken());
        return tokens;
    }

    @Override
    public AuthTokens login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (AuthenticationException ex) {
            log.warn("Login failed: invalid credentials");
            throw new InvalidCredentialsException();
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.warn("Login failed: user not found");
                return new InvalidCredentialsException();
            });
        log.info("User login id={}", user.getId());
        AuthTokens tokens = new AuthTokens(
            tokenService.generateAccessToken(user),
            tokenService.generateRefreshToken(user)
        );
        storeRefreshToken(tokens.refreshToken());
        return tokens;
    }

    @Override
    @Transactional
    public AuthTokens refresh(String refreshToken) {
        RefreshTokenClaims claims;
        try {
            claims = tokenService.parseRefreshToken(refreshToken);
        } catch (InvalidTokenException | IllegalArgumentException ex) {
            log.warn("Refresh rejected: invalid token");
            throw new InvalidTokenException("Invalid refresh token", ex);
        }
        RefreshToken storedToken = refreshTokenRepository.findByTokenId(claims.tokenId())
            .orElseThrow(() -> new InvalidTokenException("Refresh token revoked"));
        if (storedToken.getRevokedAt() != null) {
            throw new InvalidTokenException("Refresh token revoked");
        }
        if (!storedToken.getUserId().equals(claims.userId())) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        storedToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(storedToken);

        User user = userRepository.findById(claims.userId())
            .orElseThrow(() -> {
                log.warn("Refresh failed: user not found id={}", claims.userId());
                return new UserNotFoundException();
            });
        AuthTokens tokens = new AuthTokens(
            tokenService.generateAccessToken(user),
            tokenService.generateRefreshToken(user)
        );
        storeRefreshToken(tokens.refreshToken());
        return tokens;
    }

    private void storeRefreshToken(String refreshToken) {
        RefreshTokenClaims claims = tokenService.parseRefreshToken(refreshToken);
        RefreshToken token = new RefreshToken();
        token.setUserId(claims.userId());
        token.setTokenId(claims.tokenId());
        token.setExpiresAt(claims.expiresAt());
        refreshTokenRepository.save(token);
    }
}
