package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.content.dto.AdminListeningRequest;
import com.ieltsascent.backend.api.content.dto.AdminListeningResponse;
import com.ieltsascent.backend.api.content.dto.AdminSpeakingPromptRequest;
import com.ieltsascent.backend.api.content.dto.AdminSpeakingPromptResponse;
import com.ieltsascent.backend.application.content.ContentAdminMapper;
import com.ieltsascent.backend.application.content.ContentAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/content")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ContentAdminController {
    private final ContentAdminService contentAdminService;

    @PostMapping("/listening")
    public ResponseEntity<ApiResponse<AdminListeningResponse>> createListening(@Valid @RequestBody AdminListeningRequest request) {
        return ApiResponseUtil.success(ContentAdminMapper.toListeningResponse(contentAdminService.createListening(request)), ApiResponseConstant.CREATED);
    }

    @GetMapping("/listening")
    public ResponseEntity<ApiResponse<PageResponse<AdminListeningResponse>>> listListening(@ParameterObject Pageable pageable) {
        Page<AdminListeningResponse> page = contentAdminService.listListening(pageable)
            .map(ContentAdminMapper::toListeningResponse);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/listening/{id}")
    public ResponseEntity<ApiResponse<AdminListeningResponse>> getListening(@PathVariable Long id) {
        return ApiResponseUtil.success(ContentAdminMapper.toListeningResponse(contentAdminService.getListening(id)), ApiResponseConstant.SUCCESS);
    }

    @PutMapping("/listening/{id}")
    public ResponseEntity<ApiResponse<AdminListeningResponse>> updateListening(
        @PathVariable Long id,
        @Valid @RequestBody AdminListeningRequest request
    ) {
        return ApiResponseUtil.success(ContentAdminMapper.toListeningResponse(contentAdminService.updateListening(id, request)), ApiResponseConstant.UPDATED);
    }

    @DeleteMapping("/listening/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteListening(@PathVariable Long id) {
        contentAdminService.deleteListening(id);
        return ApiResponseUtil.success(null, ApiResponseConstant.DELETED);
    }

    @PostMapping("/speaking-prompts")
    public ResponseEntity<ApiResponse<AdminSpeakingPromptResponse>> createSpeakingPrompt(@Valid @RequestBody AdminSpeakingPromptRequest request) {
        return ApiResponseUtil.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.createSpeakingPrompt(request)), ApiResponseConstant.CREATED);
    }

    @GetMapping("/speaking-prompts")
    public ResponseEntity<ApiResponse<PageResponse<AdminSpeakingPromptResponse>>> listSpeakingPrompts(@ParameterObject Pageable pageable) {
        Page<AdminSpeakingPromptResponse> page = contentAdminService.listSpeakingPrompts(pageable)
            .map(ContentAdminMapper::toSpeakingPromptResponse);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/speaking-prompts/{id}")
    public ResponseEntity<ApiResponse<AdminSpeakingPromptResponse>> getSpeakingPrompt(@PathVariable Long id) {
        return ApiResponseUtil.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.getSpeakingPrompt(id)), ApiResponseConstant.SUCCESS);
    }

    @PutMapping("/speaking-prompts/{id}")
    public ResponseEntity<ApiResponse<AdminSpeakingPromptResponse>> updateSpeakingPrompt(
        @PathVariable Long id,
        @Valid @RequestBody AdminSpeakingPromptRequest request
    ) {
        return ApiResponseUtil.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.updateSpeakingPrompt(id, request)), ApiResponseConstant.UPDATED);
    }

    @DeleteMapping("/speaking-prompts/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSpeakingPrompt(@PathVariable Long id) {
        contentAdminService.deleteSpeakingPrompt(id);
        return ApiResponseUtil.success(null, ApiResponseConstant.DELETED);
    }
}
