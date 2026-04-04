package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionQueryService;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing/submissions")
@RequiredArgsConstructor
public class WritingSubmissionController {
    private final WritingSubmissionService writingSubmissionService;
    private final WritingSubmissionQueryService writingSubmissionQueryService;

    @PostMapping("/start")
    public ApiResponse<WritingDtos.SubmissionResponse> start(
        Authentication authentication,
        @Valid @RequestBody WritingDtos.StartSubmissionRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.start(userId, request.promptId(), request.timedMode());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PutMapping("/{id}/draft")
    public ApiResponse<WritingDtos.SubmissionResponse> saveDraft(
        Authentication authentication,
        @PathVariable UUID id,
        @Valid @RequestBody WritingDtos.SaveDraftRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.saveDraft(userId, id, request.essayText());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<WritingDtos.SubmissionResponse> submit(
        Authentication authentication,
        @PathVariable UUID id,
        @Valid @RequestBody WritingDtos.SubmitWritingRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.submit(userId, id, request.essayText());
        return ApiResponse.success(writingSubmissionQueryService.fromSubmission(submission));
    }

    @GetMapping("/{id}")
    public ApiResponse<WritingDtos.SubmissionResponse> getSubmission(Authentication authentication, @PathVariable UUID id) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(writingSubmissionQueryService.getOwned(userId, id));
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<WritingDtos.SubmissionResponse>> history(
        Authentication authentication,
        @ParameterObject Pageable pageable
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        Page<WritingDtos.SubmissionResponse> page = writingSubmissionQueryService.history(userId, pageable);
        return ApiResponse.success(PageResponse.from(page));
    }
}

