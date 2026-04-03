package com.ieltsascent.backend.api.user;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.auth.UserService;
import com.ieltsascent.backend.domain.auth.UserProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(Authentication authentication) {
        var userId = SecurityUtils.currentUserId(authentication);
        var user = userService.getUser(userId);
        return ApiResponse.success(UserResponse.from(user, userService.resolveReadiness(userId)));
    }

    @PatchMapping("/me/target-band")
    public ApiResponse<UserResponse> updateTargetBand(
        Authentication authentication,
        @Valid @RequestBody TargetBandRequest request
    ) {
        var userId = SecurityUtils.currentUserId(authentication);
        var user = userService.updateTargetBand(userId, request.targetBand());
        return ApiResponse.success(UserResponse.from(user, userService.resolveReadiness(userId)));
    }

    @PatchMapping("/me/baseline")
    public ApiResponse<UserResponse> updateBaseline(
        Authentication authentication,
        @Valid @RequestBody BaselineRequest request
    ) {
        var userId = SecurityUtils.currentUserId(authentication);
        var user = userService.updateBaseline(userId, request.currentBand(), request.targetBand());
        return ApiResponse.success(UserResponse.from(user, userService.resolveReadiness(userId)));
    }

    @PatchMapping("/me/exam-date")
    public ApiResponse<UserResponse> updateExamDate(
        Authentication authentication,
        @Valid @RequestBody ExamDateRequest request
    ) {
        var userId = SecurityUtils.currentUserId(authentication);
        var user = userService.updateExamDate(userId, request.examDate());
        return ApiResponse.success(UserResponse.from(user, userService.resolveReadiness(userId)));
    }

    @PatchMapping("/me/onboarding")
    public ApiResponse<UserResponse> updateOnboarding(
        Authentication authentication,
        @Valid @RequestBody OnboardingRequest request
    ) {
        var userId = SecurityUtils.currentUserId(authentication);
        var user = userService.updateOnboarding(
            userId,
            request.completed(),
            request.studyPreference()
        );
        return ApiResponse.success(UserResponse.from(user, userService.resolveReadiness(userId)));
    }

    public record TargetBandRequest(@NotNull @DecimalMin("4.0") Double targetBand) {
    }

    public record BaselineRequest(
        @NotNull @DecimalMin("1.0") Double currentBand,
        @NotNull @DecimalMin("4.0") Double targetBand
    ) {
    }

    public record ExamDateRequest(@NotNull LocalDate examDate) {
    }

    public record OnboardingRequest(
        @NotNull Boolean completed,
        UserProfile.StudyPreference studyPreference
    ) {
    }
}
