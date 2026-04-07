package com.ieltsascent.backend.application.auth;

import java.util.List;

public record AccessTokenClaims(Long userId, String email, List<String> roles) {
}
