package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.auth.UserProfile;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DiagnosticResultRepository diagnosticResultRepository;
    private final CurrentUserProvider currentUserProvider;

    public User getCurrentUser() {
        return getUser(currentUserProvider.userId());
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User updateTargetBand(Long userId, double targetBand) {
        User user = getUser(userId);
        user.getProfile().setTargetBand(targetBand);
        return userRepository.save(user);
    }

    public User updateTargetBand(double targetBand) {
        return updateTargetBand(currentUserProvider.userId(), targetBand);
    }

    public User updateBaseline(Long userId, double currentBand, double targetBand) {
        User user = getUser(userId);
        user.getProfile().setCurrentBand(currentBand);
        user.getProfile().setTargetBand(targetBand);
        user.getProfile().setOnboardingCompleted(true);
        user.getProfile().setOnboardingCompletedAt(Instant.now());
        return userRepository.save(user);
    }

    public User updateBaseline(double currentBand, double targetBand) {
        return updateBaseline(currentUserProvider.userId(), currentBand, targetBand);
    }

    public User updateExamDate(Long userId, LocalDate examDate) {
        User user = getUser(userId);
        user.getProfile().setExamDate(examDate);
        return userRepository.save(user);
    }

    public User updateExamDate(LocalDate examDate) {
        return updateExamDate(currentUserProvider.userId(), examDate);
    }

    public User updateOnboarding(Long userId, boolean completed, UserProfile.StudyPreference studyPreference) {
        User user = getUser(userId);
        user.getProfile().setOnboardingCompleted(completed);
        user.getProfile().setOnboardingCompletedAt(completed ? Instant.now() : null);
        if (studyPreference != null) {
            user.getProfile().setStudyPreference(studyPreference);
        }
        return userRepository.save(user);
    }

    public User updateOnboarding(boolean completed, UserProfile.StudyPreference studyPreference) {
        return updateOnboarding(currentUserProvider.userId(), completed, studyPreference);
    }

    public ProfileReadiness resolveReadiness(Long userId) {
        User user = getUser(userId);
        boolean hasDiagnostic = diagnosticResultRepository.existsBySessionUserId(userId);
        boolean hasBaselineBands = user.getProfile() != null
            && user.getProfile().getCurrentBand() != null
            && user.getProfile().getTargetBand() != null;
        if (hasDiagnostic || hasBaselineBands) {
            return ProfileReadiness.ready(hasDiagnostic, hasBaselineBands);
        }
        return ProfileReadiness.requiresSetup(hasDiagnostic, hasBaselineBands);
    }

    public ProfileReadiness resolveReadiness() {
        return resolveReadiness(currentUserProvider.userId());
    }
}
