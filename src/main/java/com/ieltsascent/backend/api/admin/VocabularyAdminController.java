package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.vocabulary.VocabularyAdminService;
import com.ieltsascent.backend.application.vocabulary.VocabularyAiEnrichmentService;
import com.ieltsascent.backend.application.vocabulary.VocabularyMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/vocabulary")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class VocabularyAdminController {
    private final VocabularyAdminService vocabularyAdminService;
    private final VocabularyAiEnrichmentService vocabularyAiEnrichmentService;

    @PostMapping("/words")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyWordResponse>> createWord(
        @Valid @RequestBody VocabularyDtos.VocabularyWordUpsertRequest request
    ) {
        return ApiResponseUtil.success(VocabularyMapper.toWordResponse(vocabularyAdminService.createWord(request)), ApiResponseConstant.CREATED);
    }

    @PutMapping("/words/{wordId}")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyWordResponse>> updateWord(
        @PathVariable Long wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyWordUpsertRequest request
    ) {
        return ApiResponseUtil.success(VocabularyMapper.toWordResponse(vocabularyAdminService.updateWord(wordId, request)), ApiResponseConstant.UPDATED);
    }

    @PatchMapping("/words/{wordId}/status")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyWordResponse>> updateStatus(
        @PathVariable Long wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyStatusUpdateRequest request
    ) {
        return ApiResponseUtil.success(VocabularyMapper.toWordResponse(vocabularyAdminService.updateStatus(wordId, request.status())), ApiResponseConstant.UPDATED);
    }

    @GetMapping("/words")
    public ResponseEntity<ApiResponse<PageResponse<VocabularyDtos.VocabularyWordResponse>>> listWords(
        @Valid @ModelAttribute VocabularyDtos.VocabularyWordFilterRequest filter,
        @ParameterObject Pageable pageable
    ) {
        Page<VocabularyDtos.VocabularyWordResponse> page = vocabularyAdminService
            .listWords(filter.status(), filter.level(), filter.premium(), filter.tag(), filter.query(), pageable)
            .map(VocabularyMapper::toWordResponse);
        return ApiResponseUtil.success(PageResponse.from(page), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/words/{wordId}")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyWordResponse>> getWord(@PathVariable Long wordId) {
        return ApiResponseUtil.success(VocabularyMapper.toWordResponse(vocabularyAdminService.getWord(wordId)), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/words/{wordId}/examples")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyExampleResponse>> addExample(
        @PathVariable Long wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyExampleRequest request
    ) {
        return ApiResponseUtil.success(VocabularyMapper.toExampleResponse(vocabularyAdminService.addExample(wordId, request)), ApiResponseConstant.CREATED);
    }

    @PutMapping("/examples/{exampleId}")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyExampleResponse>> updateExample(
        @PathVariable Long exampleId,
        @Valid @RequestBody VocabularyDtos.VocabularyExampleRequest request
    ) {
        return ApiResponseUtil.success(VocabularyMapper.toExampleResponse(vocabularyAdminService.updateExample(exampleId, request)), ApiResponseConstant.UPDATED);
    }

    @DeleteMapping("/examples/{exampleId}")
    public ResponseEntity<ApiResponse<Void>> deleteExample(@PathVariable Long exampleId) {
        vocabularyAdminService.deleteExample(exampleId);
        return ApiResponseUtil.success(null, ApiResponseConstant.DELETED);
    }

    @PostMapping("/ai/enrich")
    public ResponseEntity<ApiResponse<VocabularyDtos.AiEnrichmentResponse>> enrichWithAi(
        @Valid @RequestBody VocabularyDtos.AiEnrichmentRequest request
    ) {
        return ApiResponseUtil.success(vocabularyAiEnrichmentService.enrich(request), ApiResponseConstant.SUCCESS);
    }
}
