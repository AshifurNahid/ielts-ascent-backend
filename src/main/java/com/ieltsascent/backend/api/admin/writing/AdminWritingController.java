package com.ieltsascent.backend.api.admin.writing;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<PageResponse<WritingDtos.PromptResponse>>> prompts(@ParameterObject Pageable pageable) {
        return ApiResponseUtil.success(adminWritingFacadeService.prompts(pageable), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/prompts")
    public ResponseEntity<ApiResponse<WritingDtos.PromptResponse>> createPrompt(@Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponseUtil.success(adminWritingFacadeService.createPrompt(request), ApiResponseConstant.CREATED, HttpStatus.CREATED);
    }

    @PutMapping("/prompts/{id}")
    public ResponseEntity<ApiResponse<WritingDtos.PromptResponse>> updatePrompt(@PathVariable Long id, @Valid @RequestBody WritingDtos.AdminPromptRequest request) {
        return ApiResponseUtil.success(adminWritingFacadeService.updatePrompt(id, request), ApiResponseConstant.UPDATED);
    }

    @PutMapping("/prompts/{id}/status")
    public ResponseEntity<ApiResponse<WritingDtos.PromptResponse>> setPromptStatus(
        @PathVariable Long id,
        @Valid @ModelAttribute WritingDtos.PromptStatusRequest request
    ) {
        return ApiResponseUtil.success(adminWritingFacadeService.setPromptStatus(id, request.status()), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<List<WritingDtos.TemplateResponse>>> templates(
        @Valid @ModelAttribute WritingDtos.TemplateFilterRequest filter
    ) {
        return ApiResponseUtil.success(adminWritingFacadeService.templates(filter.taskType(), filter.promptId()), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/templates")
    public ResponseEntity<ApiResponse<WritingDtos.TemplateResponse>> createTemplate(@Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponseUtil.success(adminWritingFacadeService.createTemplate(request), ApiResponseConstant.CREATED, HttpStatus.CREATED);
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<WritingDtos.TemplateResponse>> updateTemplate(@PathVariable Long id, @Valid @RequestBody WritingDtos.AdminTemplateRequest request) {
        return ApiResponseUtil.success(adminWritingFacadeService.updateTemplate(id, request), ApiResponseConstant.UPDATED);
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<ApiResponse<Void>> archiveTemplate(@PathVariable Long id) {
        adminWritingFacadeService.archiveTemplate(id);
        return ApiResponseUtil.success(null, ApiResponseConstant.DELETED);
    }
}
