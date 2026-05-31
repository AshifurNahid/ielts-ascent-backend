package com.ieltsascent.backend.api.admin.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.writing.core.AdminWritingFacadeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/writing")
@RequiredArgsConstructor
public class AdminWritingController {
    private final AdminWritingFacadeService adminWritingFacadeService;

    @GetMapping("/prompts")
    public ApiResponse<PageResponse<WritingDtos.PromptResponse>> prompts(@ParameterObject Pageable pageable) {
        return ApiResponse.success(adminWritingFacadeService.prompts(pageable));
    }

    @PostMapping("/prompts")
    public ApiResponse<WritingDtos.PromptResponse> createPrompt(@Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponse.success(adminWritingFacadeService.createPrompt(request));
    }

    @PutMapping("/prompts/{id}")
    public ApiResponse<WritingDtos.PromptResponse> updatePrompt(@PathVariable Long id, @Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponse.success(adminWritingFacadeService.updatePrompt(id, request));
    }

    @PutMapping("/prompts/{id}/status")
    public ApiResponse<WritingDtos.PromptResponse> setPromptStatus(
        @PathVariable Long id,
        @Valid @ModelAttribute WritingDtos.PromptStatusRequest request
    ) {
        return ApiResponse.success(adminWritingFacadeService.setPromptStatus(id, request.status()));
    }

    @GetMapping("/templates")
    public ApiResponse<List<WritingDtos.TemplateResponse>> templates(
        @Valid @ModelAttribute WritingDtos.TemplateFilterRequest filter
    ) {
        return ApiResponse.success(adminWritingFacadeService.templates(filter.taskType(), filter.promptId()));
    }

    @PostMapping("/templates")
    public ApiResponse<WritingDtos.TemplateResponse> createTemplate(@Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponse.success(adminWritingFacadeService.createTemplate(request));
    }

    @PutMapping("/templates/{id}")
    public ApiResponse<WritingDtos.TemplateResponse> updateTemplate(@PathVariable Long id, @Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponse.success(adminWritingFacadeService.updateTemplate(id, request));
    }

    @DeleteMapping("/templates/{id}")
    public ApiResponse<Void> archiveTemplate(@PathVariable Long id) {
        adminWritingFacadeService.archiveTemplate(id);
        return ApiResponse.success(null);
    }
}
