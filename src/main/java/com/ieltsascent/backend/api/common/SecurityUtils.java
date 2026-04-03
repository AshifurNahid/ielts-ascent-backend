package com.ieltsascent.backend.api.common;

import com.ieltsascent.backend.application.auth.exception.UnauthenticatedException;
import java.util.UUID;
import org.springframework.security.core.Authentication;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static UUID currentUserId(Authentication authentication) {
        if (authentication == null) {
            throw new UnauthenticatedException();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UUID uuid) {
            return uuid;
        }
        String name = authentication.getName();
        if (name == null || name.isBlank()) {
            throw new UnauthenticatedException();
        }
        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException ex) {
            throw new UnauthenticatedException();
        }
    }
}
