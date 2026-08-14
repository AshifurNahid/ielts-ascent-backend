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
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
        String normalizedEmail = normalizeEmail(email);
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        UserProfile profile = new UserProfile();
        profile.setTargetBand(7.0);
        profile.setExamDate(LocalDate.now().plusMonths(3));
        profile.setTimezone("UTC");
        profileRepository.save(profile);

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(Role.STUDENT);
        user.setProfile(profile);
        userRepository.save(user);

        return generateTokenAndResponse(user);
    }

    @Override
    @Transactional
    public AuthTokens login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(normalizedEmail, password)
        );
        User user = extractAuthenticatedUser(authentication);
        return generateTokenAndResponse(user);
    }

    @Override
    @Transactional
    public AuthTokens refresh(String refreshToken) {
        RefreshTokenClaims claims;
        try {
            claims = tokenService.parseRefreshToken(refreshToken);
        } catch (InvalidTokenException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid refresh token", ex);
        }
        RefreshToken storedToken = refreshTokenRepository.findByTokenIdAndRevokedAtIsNull(claims.tokenId())
            .orElseThrow(() -> new InvalidTokenException("Refresh token revoked"));
        if (!storedToken.getUserId().equals(claims.userId())) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        if (storedToken.getExpiresAt().isBefore(Instant.now())) {
            storedToken.setRevokedAt(Instant.now());
            refreshTokenRepository.save(storedToken);
            throw new InvalidTokenException("Refresh token expired");
        }
        storedToken.setRevokedAt(Instant.now());
        refreshTokenRepository.save(storedToken);

        User user = userRepository.findById(claims.userId())
            .orElseThrow(UserNotFoundException::new);
        return generateTokenAndResponse(user);
    }

    private AuthTokens generateTokenAndResponse(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);
        storeRefreshToken(refreshToken);

        UserProfile profile = user.getProfile();
        return new AuthTokens(
            "Bearer",
            accessToken,
            refreshToken,
            user.getId(),
            user.getEmail(),
            user.getFullName(),
            user.getRole(),
            profile != null ? profile.getTargetBand() : null,
            profile != null ? profile.getCurrentBand() : null,
            profile != null ? profile.getOnboardingCompleted() : null
        );
    }

    private void storeRefreshToken(String refreshToken) {
        RefreshTokenClaims claims = tokenService.parseRefreshToken(refreshToken);
        RefreshToken token = new RefreshToken();
        token.setUserId(claims.userId());
        token.setTokenId(claims.tokenId());
        token.setExpiresAt(claims.expiresAt());
        refreshTokenRepository.save(token);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private User extractAuthenticatedUser(Authentication authentication) {
        if (authentication == null) {
            throw new InvalidCredentialsException();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new InvalidCredentialsException();
    }
}
