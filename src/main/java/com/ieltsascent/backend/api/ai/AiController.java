package com.ieltsascent.backend.api.ai;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.ai.AiAdminService;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.application.ai.ShadowingComparisonResult;
import com.ieltsascent.backend.application.ai.StoryOutline;
import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AiController {
    private final AiAdminService aiAdminService;

    @PostMapping("/speaking/evaluate")
    public ApiResponse<SpeakingEvaluationResult> evaluateSpeaking(@Valid @RequestBody SpeakingEvaluateRequest request) {
        return ApiResponse.success(aiAdminService.evaluateSpeaking(request.audioUrl(), request.durationSeconds()));
    }

    @PostMapping("/speaking/story-builder")
    public ApiResponse<StoryOutline> storyBuilder(@Valid @RequestBody StoryBuilderRequest request) {
        return ApiResponse.success(aiAdminService.storyBuilder(request.topic()));
    }

    @PostMapping("/speaking/shadowing/analysis")
    public ApiResponse<ShadowingComparisonResult> shadowing(@Valid @RequestBody ShadowingRequest request) {
        return ApiResponse.success(
            aiAdminService.analyzeShadowing(request.audioUrl(), request.durationSeconds(), request.referenceSampleId())
        );
    }

    @PostMapping("/recommendations/next-actions")
    public ApiResponse<PageResponse<StudyTaskResponse>> nextActions(
        Authentication authentication,
        @ParameterObject Pageable pageable
    ) {
        List<StudyTaskResponse> tasks = aiAdminService.nextActions(SecurityUtils.currentUserId(authentication))
            .stream()
            .map(StudyTaskResponse::from)
            .toList();
        return ApiResponse.success(PageResponse.from(tasks, pageable));
    }

    @PostMapping("/exam-prediction")
    public ApiResponse<ExamPrediction> examPrediction(Authentication authentication) {
        return ApiResponse.success(aiAdminService.examPrediction(SecurityUtils.currentUserId(authentication)));
    }

    public record SpeakingEvaluateRequest(@NotBlank String audioUrl, @NotNull Integer durationSeconds) {
    }

    public record StoryBuilderRequest(@NotBlank String topic) {
    }

    public record ShadowingRequest(
        @NotBlank String audioUrl,
        @NotNull Integer durationSeconds,
        @NotBlank String referenceSampleId
    ) {
    }

    public record StudyTaskResponse(UUID id, String title, String taskType, Integer estimatedMinutes) {
        public static StudyTaskResponse from(StudyTask task) {
            return new StudyTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getTaskType(),
                task.getEstimatedMinutes()
            );
        }
    }
}
