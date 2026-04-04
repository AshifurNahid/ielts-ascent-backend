package com.ieltsascent.backend.api.practice;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.application.content.ContentService;
import com.ieltsascent.backend.application.practice.PracticeService;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.practice.PracticeSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {
    private final PracticeService practiceService;
    private final ContentService contentService;

    @GetMapping("/listening/tasks")
    public ApiResponse<PageResponse<ListeningTaskDto>> listeningTasks(@ParameterObject Pageable pageable) {
        Page<ListeningTaskDto> page = contentService.listListeningAudios(pageable)
            .map(ListeningTaskDto::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @PostMapping("/listening/{taskId}/submit")
    public ApiResponse<PracticeCompletionResponse> submitListening(Authentication authentication, @PathVariable UUID taskId) {
        PracticeSession session = practiceService.recordListeningCompletion(
            SecurityUtils.currentUserId(authentication),
            taskId
        );
        return ApiResponse.success(
            new PracticeCompletionResponse(session.getId(), session.getTaskId(), session.getCompletedAt().toString())
        );
    }

    @GetMapping("/speaking/prompts")
    public ApiResponse<PageResponse<SpeakingPromptDto>> speakingPrompts(@ParameterObject Pageable pageable) {
        Page<SpeakingPromptDto> page = practiceService.listSpeakingPrompts(pageable)
            .map(SpeakingPromptDto::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @PostMapping("/speaking/{promptId}/submit")
    public ApiResponse<SpeakingSubmissionResponse> submitSpeaking(
        Authentication authentication,
        @PathVariable UUID promptId,
        @Valid @RequestBody SpeakingSubmissionRequest request
    ) {
        SpeakingRecordingMetadata recording = practiceService.submitSpeaking(
            SecurityUtils.currentUserId(authentication),
            promptId,
            request.audioUrl(),
            request.durationSeconds()
        );
        return ApiResponse.success(SpeakingSubmissionResponse.from(recording));
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<PracticeSessionResponse>> history(
        Authentication authentication,
        @RequestParam(required = false) String skillType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @ParameterObject @PageableDefault(sort = "completedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PracticeSessionResponse> page = practiceService.listHistory(
                SecurityUtils.currentUserId(authentication),
                skillType,
                from,
                to,
                pageable
            )
            .map(PracticeSessionResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    public record SpeakingPromptDto(UUID id, String title, String description, String part) {
        public static SpeakingPromptDto from(SpeakingPrompt prompt) {
            return new SpeakingPromptDto(prompt.getId(), prompt.getTitle(), prompt.getDescription(), prompt.getPart());
        }
    }

    public record SpeakingSubmissionRequest(@NotBlank String audioUrl, @NotNull Integer durationSeconds) {
    }

    public record SpeakingSubmissionResponse(UUID recordingId, Double overallBand, String feedbackSummary) {
        public static SpeakingSubmissionResponse from(SpeakingRecordingMetadata recording) {
            return new SpeakingSubmissionResponse(
                recording.getId(),
                recording.getEvaluationResult().getOverallBand(),
                recording.getEvaluationResult().getFeedbackSummary()
            );
        }
    }

    public record ListeningTaskDto(UUID id, String title, String description, Integer durationSeconds, String audioUrl) {
        public static ListeningTaskDto from(ListeningAudio audio) {
            return new ListeningTaskDto(
                audio.getId(),
                audio.getTitle(),
                audio.getDescription(),
                audio.getDurationSeconds(),
                audio.getAudioUrl()
            );
        }
    }


    public record PracticeCompletionResponse(UUID sessionId, UUID taskId, String completedAt) {
    }

    public record PracticeSessionResponse(
        UUID sessionId,
        String skillType,
        UUID taskId,
        Instant startedAt,
        Instant completedAt
    ) {
        public static PracticeSessionResponse from(PracticeSession session) {
            return new PracticeSessionResponse(
                session.getId(),
                session.getSkillType(),
                session.getTaskId(),
                session.getStartedAt(),
                session.getCompletedAt()
            );
        }
    }
}
