package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingAiService;
import com.ieltsascent.backend.application.writing.core.WritingSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing/submissions")
@RequiredArgsConstructor
public class WritingAssistanceController {
    private final WritingSubmissionService writingSubmissionService;
    private final WritingAiService writingAiService;

    @PostMapping("/{id}/improve-section")
    public ApiResponse<String> improveSection(
        @PathVariable Long id,
        @Valid @RequestBody WritingDtos.ImproveSectionRequest request
    ) {
        var submission = writingSubmissionService.findSubmissionOwnedByUser(id);
        String result = writingAiService.improveSection(
            submission.getPrompt() == null ? "" : submission.getPrompt().getPromptText(),
            submission.getEssayText(),
            request.sectionText(),
            request.instruction()
        );
        return ApiResponse.success(result);
    }
}
