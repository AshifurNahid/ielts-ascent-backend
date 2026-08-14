package com.ieltsascent.backend.api.practice;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.application.practice.PracticeApiService;
import com.ieltsascent.backend.application.practice.dto.PracticeCompletionDto;
import com.ieltsascent.backend.application.practice.dto.PracticeListeningTaskDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSessionItemDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSpeakingPromptDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSpeakingSubmissionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
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
    private final PracticeApiService practiceApiService;

    @GetMapping("/listening/tasks")
    public ResponseEntity<ApiResponse<PageResponse<PracticeListeningTaskDto>>> listeningTasks(@ParameterObject Pageable pageable) {
        return ApiResponseUtil.success(practiceApiService.listeningTasks(pageable), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/listening/{taskId}/submit")
    public ResponseEntity<ApiResponse<PracticeCompletionDto>> submitListening(@PathVariable Long taskId) {
        return ApiResponseUtil.success(practiceApiService.submitListening(taskId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/speaking/prompts")
    public ResponseEntity<ApiResponse<PageResponse<PracticeSpeakingPromptDto>>> speakingPrompts(@ParameterObject Pageable pageable) {
        return ApiResponseUtil.success(practiceApiService.speakingPrompts(pageable), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/speaking/{promptId}/submit")
    public ResponseEntity<ApiResponse<PracticeSpeakingSubmissionDto>> submitSpeaking(
        @PathVariable Long promptId,
        @Valid @RequestBody SpeakingSubmissionRequest request
    ) {
        return ApiResponseUtil.success(
            practiceApiService.submitSpeaking(
                promptId,
                request.audioUrl(),
                request.durationSeconds()
            )
            , ApiResponseConstant.CREATED
        );
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<PracticeSessionItemDto>>> history(
        @RequestParam(required = false) String skillType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @ParameterObject @PageableDefault(sort = "completedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponseUtil.success(
            practiceApiService.history(skillType, from, to, pageable)
            , ApiResponseConstant.SUCCESS
        );
    }

    public record SpeakingSubmissionRequest(@NotBlank String audioUrl, @NotNull Integer durationSeconds) {
    }
}
