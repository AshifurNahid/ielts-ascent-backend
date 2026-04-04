package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingPromptService;
import com.ieltsascent.backend.application.writing.core.WritingTemplateService;
import com.ieltsascent.backend.domain.writing.WritingDifficulty;
import com.ieltsascent.backend.domain.writing.WritingTaskType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingCatalogController {
    private final WritingPromptService writingPromptService;
    private final WritingTemplateService writingTemplateService;

    @GetMapping("/prompts")
    public ApiResponse<PageResponse<WritingDtos.PromptResponse>> prompts(
        @RequestParam(required = false) WritingTaskType taskType,
        @RequestParam(required = false) WritingDifficulty difficulty,
        @ParameterObject Pageable pageable
    ) {
        Page<WritingDtos.PromptResponse> page = writingPromptService
            .listPublished(taskType, difficulty, pageable)
            .map(WritingDtos.PromptResponse::from);
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
        return ApiResponse.success(
            writingTemplateService.listActive(taskType, promptId).stream().map(WritingDtos.TemplateResponse::from).toList()
        );
    }
}

