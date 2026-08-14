package com.ieltsascent.backend.application.auth;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.auth.UserProfile;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        DiagnosticResultRepository diagnosticResultRepository = mock(DiagnosticResultRepository.class);
        CurrentUserProvider currentUserProvider = mock(CurrentUserProvider.class);
        userService = new UserService(userRepository, diagnosticResultRepository, currentUserProvider);
    }

    @Test
    void updateOnboardingMarksProfileAndPreference() {
        Long userId = 1L;
        UserProfile profile = new UserProfile();
        profile.setTargetBand(6.5);
        User user = new User();
        user.setProfile(profile);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateOnboarding(userId, true, UserProfile.StudyPreference.INTENSIVE);

        assertThat(updated.getProfile().getOnboardingCompleted()).isTrue();
        assertThat(updated.getProfile().getOnboardingCompletedAt()).isNotNull();
        assertThat(updated.getProfile().getStudyPreference()).isEqualTo(UserProfile.StudyPreference.INTENSIVE);
    }
}
