package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.application.auth.exception.InvalidCredentialsException;
import com.ieltsascent.backend.application.auth.exception.InvalidTokenException;
import com.ieltsascent.backend.domain.auth.RefreshToken;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.auth.UserProfile;
import com.ieltsascent.backend.infrastructure.persistence.RefreshTokenRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    private UserRepository userRepository;
    private UserProfileRepository profileRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        profileRepository = mock(UserProfileRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        tokenService = mock(TokenService.class);
        authService = new AuthService(
            userRepository,
            profileRepository,
            refreshTokenRepository,
            passwordEncoder,
            authenticationManager,
            tokenService
        );
    }

    @Test
    void registerStoresRefreshToken() {
        when(userRepository.findByEmail("jake@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("hashed");
        when(profileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenService.generateAccessToken(any(User.class))).thenReturn("access");
        when(tokenService.generateRefreshToken(any(User.class))).thenReturn("refresh");
        RefreshTokenClaims claims = new RefreshTokenClaims(
            UUID.randomUUID(),
            UUID.randomUUID().toString(),
            Instant.now().plusSeconds(3600)
        );
        when(tokenService.parseRefreshToken("refresh")).thenReturn(claims);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthTokens tokens = authService.register("Jake", "jake@example.com", "secret");

        assertThat(tokens.accessToken()).isEqualTo("access");
        assertThat(tokens.refreshToken()).isEqualTo("refresh");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(claims.userId());
        assertThat(saved.getTokenId()).isEqualTo(claims.tokenId());
        assertThat(saved.getExpiresAt()).isEqualTo(claims.expiresAt());
    }

    @Test
    void loginRejectsInvalidCredentials() {
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("bad"));

        assertThatThrownBy(() -> authService.login("jake@example.com", "secret"))
            .isInstanceOf(InvalidCredentialsException.class);

        verifyNoInteractions(tokenService);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void refreshRotatesRefreshToken() {
        UUID userId = UUID.randomUUID();
        String oldTokenId = UUID.randomUUID().toString();
        String newTokenId = UUID.randomUUID().toString();
        RefreshTokenClaims oldClaims = new RefreshTokenClaims(userId, oldTokenId, Instant.now().plusSeconds(600));
        RefreshTokenClaims newClaims = new RefreshTokenClaims(userId, newTokenId, Instant.now().plusSeconds(1200));

        when(tokenService.parseRefreshToken("refresh-old")).thenReturn(oldClaims);
        when(tokenService.parseRefreshToken("refresh-new")).thenReturn(newClaims);
        RefreshToken stored = new RefreshToken();
        stored.setUserId(userId);
        stored.setTokenId(oldTokenId);
        stored.setExpiresAt(oldClaims.expiresAt());
        when(refreshTokenRepository.findByTokenId(oldTokenId)).thenReturn(Optional.of(stored));

        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(tokenService.generateAccessToken(user)).thenReturn("access-new");
        when(tokenService.generateRefreshToken(user)).thenReturn("refresh-new");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthTokens tokens = authService.refresh("refresh-old");

        assertThat(tokens.accessToken()).isEqualTo("access-new");
        assertThat(tokens.refreshToken()).isEqualTo("refresh-new");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository, times(2)).save(captor.capture());
        List<RefreshToken> saved = captor.getAllValues();
        assertThat(saved.get(0).getTokenId()).isEqualTo(oldTokenId);
        assertThat(saved.get(0).getRevokedAt()).isNotNull();
        assertThat(saved.get(1).getTokenId()).isEqualTo(newTokenId);
    }

    @Test
    void refreshRejectsUnknownToken() {
        RefreshTokenClaims claims = new RefreshTokenClaims(
            UUID.randomUUID(),
            UUID.randomUUID().toString(),
            Instant.now().plusSeconds(600)
        );
        when(tokenService.parseRefreshToken("refresh")).thenReturn(claims);
        when(refreshTokenRepository.findByTokenId(claims.tokenId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh("refresh"))
            .isInstanceOf(InvalidTokenException.class);

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }
}
