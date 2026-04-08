package com.ieltsascent.backend.api.user;

import com.ieltsascent.backend.application.auth.ProfileReadiness;
import com.ieltsascent.backend.domain.auth.User;
import java.time.LocalDate;

public record UserResponse(
    Long id,
    String fullName,
    String email,
    Double currentBand,
    Double targetBand,
    LocalDate examDate,
    Boolean onboardingCompleted,
    ProfileReadiness readiness
) {
    public static UserResponse from(User user, ProfileReadiness readiness) {
        return new UserResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getProfile() != null ? user.getProfile().getCurrentBand() : null,
            user.getProfile() != null ? user.getProfile().getTargetBand() : null,
            user.getProfile() != null ? user.getProfile().getExamDate() : null,
            user.getProfile() != null ? user.getProfile().getOnboardingCompleted() : null,
            readiness
        );
    }
}
