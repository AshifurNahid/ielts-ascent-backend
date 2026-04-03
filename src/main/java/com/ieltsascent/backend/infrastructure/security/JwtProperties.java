package com.ieltsascent.backend.infrastructure.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(
    @NotBlank String issuer,
    @Positive long accessTokenTtl,
    @Positive long refreshTokenTtl,
    @NotBlank @Size(min = 32) String secret
) {
}
