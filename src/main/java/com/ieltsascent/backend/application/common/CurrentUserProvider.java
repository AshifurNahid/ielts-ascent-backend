package com.ieltsascent.backend.application.common;

import com.ieltsascent.backend.application.auth.exception.UnauthenticatedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {
    public Long userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
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

