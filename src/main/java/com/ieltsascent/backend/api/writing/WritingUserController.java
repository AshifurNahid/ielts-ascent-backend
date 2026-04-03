package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.UserWritingProgressService;
import com.ieltsascent.backend.application.writing.core.WritingAiService;
import com.ieltsascent.backend.application.writing.core.WritingPromptService;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionService;
import com.ieltsascent.backend.application.writing.core.WritingTemplateService;
import com.ieltsascent.backend.application.writing.core.WritingWeakPointService;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSuggestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingWeakPointRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingUserController {
    private final WritingPromptService writingPromptService;
    private final WritingTemplateService writingTemplateService;
    private final WritingSubmissionService writingSubmissionService;
    private final WritingWeakPointService writingWeakPointService;
    private final UserWritingProgressService userWritingProgressService;
    private final WritingSuggestionRepository writingSuggestionRepository;
    private final WritingWeakPointRepository writingWeakPointRepository;
    private final WritingAiService writingAiService;

    @GetMapping("/prompts")
    public ApiResponse<PageResponse<WritingDtos.PromptResponse>> prompts(
        @RequestParam(required = false) WritingTaskType taskType,
        @RequestParam(required = false) WritingDifficulty difficulty,
        @ParameterObject Pageable pageable
    ) {
        Page<WritingDtos.PromptResponse> page = writingPromptService.listPublished(taskType, difficulty, pageable).map(WritingDtos.PromptResponse::from);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/prompts/{id}")
    public ApiResponse<WritingDtos.PromptResponse> getPrompt(@PathVariable UUID id) {
        return ApiResponse.success(WritingDtos.PromptResponse.from(writingPromptService.get(id)));
    }

    @GetMapping("/templates")
    public ApiResponse<List<WritingDtos.TemplateResponse>> templates(
        @RequestParam(required = false) WritingTaskType taskType,
        @RequestParam(required = false) UUID promptId
    ) {
        return ApiResponse.success(writingTemplateService.listActive(taskType, promptId).stream().map(WritingDtos.TemplateResponse::from).toList());
    }

    @PostMapping("/submissions/start")
    public ApiResponse<WritingDtos.SubmissionResponse> start(Authentication authentication, @Valid @RequestBody WritingDtos.StartSubmissionRequest request) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.start(userId, request.promptId(), request.timedMode());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PutMapping("/submissions/{id}/draft")
    public ApiResponse<WritingDtos.SubmissionResponse> saveDraft(
        Authentication authentication,
        @PathVariable UUID id,
        @Valid @RequestBody WritingDtos.SaveDraftRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.saveDraft(userId, id, request.essayText());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(submission, List.of(), List.of()));
    }

    @PostMapping("/submissions/{id}/submit")
    public ApiResponse<WritingDtos.SubmissionResponse> submit(
        Authentication authentication,
        @PathVariable UUID id,
        @Valid @RequestBody WritingDtos.SubmitWritingRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.submit(userId, id, request.essayText());
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(
            submission,
            writingWeakPointRepository.findBySubmissionId(submission.getId()),
            writingSuggestionRepository.findBySubmissionId(submission.getId())
        ));
    }

    @GetMapping("/submissions/{id}")
    public ApiResponse<WritingDtos.SubmissionResponse> getSubmission(Authentication authentication, @PathVariable UUID id) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.getOwned(userId, id);
        return ApiResponse.success(WritingDtos.SubmissionResponse.from(
            submission,
            writingWeakPointRepository.findBySubmissionId(submission.getId()),
            writingSuggestionRepository.findBySubmissionId(submission.getId())
        ));
    }

    @GetMapping("/submissions/history")
    public ApiResponse<PageResponse<WritingDtos.SubmissionResponse>> history(Authentication authentication, @ParameterObject Pageable pageable) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        Page<WritingDtos.SubmissionResponse> page = writingSubmissionService.history(userId, pageable)
            .map(s -> WritingDtos.SubmissionResponse.from(
                s,
                writingWeakPointRepository.findBySubmissionId(s.getId()),
                writingSuggestionRepository.findBySubmissionId(s.getId())
            ));
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/progress")
    public ApiResponse<WritingDtos.ProgressResponse> progress(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(WritingDtos.ProgressResponse.from(userWritingProgressService.getByUserId(userId)));
    }

    @GetMapping("/weak-points")
    public ApiResponse<List<WritingWeakPointService.RecurringWeakPointSummary>> weakPoints(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(writingWeakPointService.recurringWeakPoints(userId));
    }

    @PostMapping("/submissions/{id}/improve-section")
    public ApiResponse<String> improveSection(
        Authentication authentication,
        @PathVariable UUID id,
        @Valid @RequestBody WritingDtos.ImproveSectionRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var submission = writingSubmissionService.getOwned(userId, id);
        String result = writingAiService.improveSection(
            submission.getPrompt() == null ? "" : submission.getPrompt().getPromptText(),
            submission.getEssayText(),
            request.sectionText(),
            request.instruction()
        );
        return ApiResponse.success(result);
    }
}
