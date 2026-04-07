package com.ieltsascent.backend.api.common;

import com.ieltsascent.backend.application.auth.exception.UnauthenticatedException;
import org.springframework.security.core.Authentication;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static Long currentUserId(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthenticatedException();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        String name = authentication.getName();
        if (name == null || name.isBlank()) {
            throw new UnauthenticatedException();
        }
        try {
            return Long.parseLong(name);
        } catch (NumberFormatException ex) {
            throw new UnauthenticatedException();
        }
    }
}
