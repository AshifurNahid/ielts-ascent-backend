package com.ieltsascent.backend.api.user;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.application.auth.UserService;
import com.ieltsascent.backend.domain.auth.UserProfile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        var user = userService.getCurrentUser();
        return ApiResponseUtil.success(UserResponse.from(user, userService.resolveReadiness()), ApiResponseConstant.SUCCESS);
    }

    @PatchMapping("/me/target-band")
    public ResponseEntity<ApiResponse<UserResponse>> updateTargetBand(
        @Valid @RequestBody TargetBandRequest request
    ) {
        var user = userService.updateTargetBand(request.targetBand());
        return ApiResponseUtil.success(UserResponse.from(user, userService.resolveReadiness()), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/me/baseline")
    public ResponseEntity<ApiResponse<UserResponse>> updateBaseline(
        @Valid @RequestBody BaselineRequest request
    ) {
        var user = userService.updateBaseline(request.currentBand(), request.targetBand());
        return ApiResponseUtil.success(UserResponse.from(user, userService.resolveReadiness()), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/me/exam-date")
    public ResponseEntity<ApiResponse<UserResponse>> updateExamDate(
        @Valid @RequestBody ExamDateRequest request
    ) {
        var user = userService.updateExamDate(request.examDate());
        return ApiResponseUtil.success(UserResponse.from(user, userService.resolveReadiness()), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/me/onboarding")
    public ResponseEntity<ApiResponse<UserResponse>> updateOnboarding(
        @Valid @RequestBody OnboardingRequest request
    ) {
        var user = userService.updateOnboarding(
            request.completed(),
            request.studyPreference()
        );
        return ApiResponseUtil.success(UserResponse.from(user, userService.resolveReadiness()), ApiResponseConstant.UPDATED);
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
