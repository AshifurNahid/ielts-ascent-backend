package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
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
    public ApiResponse<AdminListeningResponse> createListening(@Valid @RequestBody AdminListeningRequest request) {
        return ApiResponse.success(ContentAdminMapper.toListeningResponse(contentAdminService.createListening(request)));
    }

    @GetMapping("/listening")
    public ApiResponse<PageResponse<AdminListeningResponse>> listListening(@ParameterObject Pageable pageable) {
        Page<AdminListeningResponse> page = contentAdminService.listListening(pageable)
            .map(ContentAdminMapper::toListeningResponse);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/listening/{id}")
    public ApiResponse<AdminListeningResponse> getListening(@PathVariable Long id) {
        return ApiResponse.success(ContentAdminMapper.toListeningResponse(contentAdminService.getListening(id)));
    }

    @PutMapping("/listening/{id}")
    public ApiResponse<AdminListeningResponse> updateListening(
        @PathVariable Long id,
        @Valid @RequestBody AdminListeningRequest request
    ) {
        return ApiResponse.success(ContentAdminMapper.toListeningResponse(contentAdminService.updateListening(id, request)));
    }

    @DeleteMapping("/listening/{id}")
    public ApiResponse<Void> deleteListening(@PathVariable Long id) {
        contentAdminService.deleteListening(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/speaking-prompts")
    public ApiResponse<AdminSpeakingPromptResponse> createSpeakingPrompt(@Valid @RequestBody AdminSpeakingPromptRequest request) {
        return ApiResponse.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.createSpeakingPrompt(request)));
    }

    @GetMapping("/speaking-prompts")
    public ApiResponse<PageResponse<AdminSpeakingPromptResponse>> listSpeakingPrompts(@ParameterObject Pageable pageable) {
        Page<AdminSpeakingPromptResponse> page = contentAdminService.listSpeakingPrompts(pageable)
            .map(ContentAdminMapper::toSpeakingPromptResponse);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/speaking-prompts/{id}")
    public ApiResponse<AdminSpeakingPromptResponse> getSpeakingPrompt(@PathVariable Long id) {
        return ApiResponse.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.getSpeakingPrompt(id)));
    }

    @PutMapping("/speaking-prompts/{id}")
    public ApiResponse<AdminSpeakingPromptResponse> updateSpeakingPrompt(
        @PathVariable Long id,
        @Valid @RequestBody AdminSpeakingPromptRequest request
    ) {
        return ApiResponse.success(ContentAdminMapper.toSpeakingPromptResponse(contentAdminService.updateSpeakingPrompt(id, request)));
    }

    @DeleteMapping("/speaking-prompts/{id}")
    public ApiResponse<Void> deleteSpeakingPrompt(@PathVariable Long id) {
        contentAdminService.deleteSpeakingPrompt(id);
        return ApiResponse.success(null);
    }
}
