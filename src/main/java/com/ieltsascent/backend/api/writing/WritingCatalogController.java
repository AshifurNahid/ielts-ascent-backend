package com.ieltsascent.backend.api.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.WritingCatalogFacadeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/writing")
@RequiredArgsConstructor
public class WritingCatalogController {
    private final WritingCatalogFacadeService writingCatalogFacadeService;

    @GetMapping("/prompts")
    public ApiResponse<PageResponse<WritingDtos.PromptResponse>> prompts(
        @Valid @ModelAttribute WritingDtos.PromptFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        return ApiResponse.success(writingCatalogFacadeService.listPrompts(filter.taskType(), filter.difficulty(), pageable));
    }

    @GetMapping("/prompts/{id}")
    public ApiResponse<WritingDtos.PromptResponse> getPrompt(@PathVariable Long id) {
        return ApiResponse.success(writingCatalogFacadeService.getPrompt(id));
    }

    @GetMapping("/templates")
    public ApiResponse<List<WritingDtos.TemplateResponse>> templates(
        @Valid @ModelAttribute WritingDtos.TemplateFilterRequest filter
    ) {
        return ApiResponse.success(writingCatalogFacadeService.listTemplates(filter.taskType(), filter.promptId()));
    }
}
