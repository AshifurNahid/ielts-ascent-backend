package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionQueryService;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        @Valid @RequestBody WritingDtos.StartSubmissionRequest request
    ) {
        var submission = writingSubmissionService.start(request.promptId(), request.timedMode());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PutMapping("/{id}/draft")
    public ApiResponse<WritingDtos.SubmissionResponse> saveDraft(
        @PathVariable Long id,
        @Valid @RequestBody WritingDtos.SaveDraftRequest request
    ) {
        var submission = writingSubmissionService.saveDraft(id, request.essayText());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<WritingDtos.SubmissionResponse> submit(
        @PathVariable Long id,
        @Valid @RequestBody WritingDtos.SubmitWritingRequest request
    ) {
        var submission = writingSubmissionService.submit(id, request.essayText());
        return ApiResponse.success(writingSubmissionQueryService.fromSubmission(submission));
    }

    @GetMapping("/{id}")
    public ApiResponse<WritingDtos.SubmissionResponse> getSubmission(@PathVariable Long id) {
        return ApiResponse.success(writingSubmissionQueryService.getOwned(id));
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<WritingDtos.SubmissionResponse>> history(
        @ParameterObject Pageable pageable
    ) {
        Page<WritingDtos.SubmissionResponse> page = writingSubmissionQueryService.history(pageable);
        return ApiResponse.success(PageResponse.from(page));
    }
}

