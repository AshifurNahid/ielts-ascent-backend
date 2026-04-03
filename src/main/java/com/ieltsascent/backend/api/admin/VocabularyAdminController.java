package com.ieltsascent.backend.api.admin;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.vocabulary.VocabularyAdminService;
import com.ieltsascent.backend.application.vocabulary.VocabularyAiEnrichmentService;
import com.ieltsascent.backend.application.vocabulary.VocabularyMapper;
import com.ieltsascent.backend.domain.vocabulary.VocabularyLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyStatus;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/vocabulary")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class VocabularyAdminController {
    private final VocabularyAdminService vocabularyAdminService;
    private final VocabularyAiEnrichmentService vocabularyAiEnrichmentService;

    @PostMapping("/words")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> createWord(
        @Valid @RequestBody VocabularyDtos.VocabularyWordUpsertRequest request
    ) {
        return ApiResponse.success(VocabularyMapper.toWordResponse(vocabularyAdminService.createWord(request)));
    }

    @PutMapping("/words/{wordId}")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> updateWord(
        @PathVariable UUID wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyWordUpsertRequest request
    ) {
        return ApiResponse.success(VocabularyMapper.toWordResponse(vocabularyAdminService.updateWord(wordId, request)));
    }

    @PatchMapping("/words/{wordId}/status")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> updateStatus(
        @PathVariable UUID wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyStatusUpdateRequest request
    ) {
        return ApiResponse.success(VocabularyMapper.toWordResponse(vocabularyAdminService.updateStatus(wordId, request.status())));
    }

    @GetMapping("/words")
    public ApiResponse<PageResponse<VocabularyDtos.VocabularyWordResponse>> listWords(
        @RequestParam(required = false) VocabularyStatus status,
        @RequestParam(required = false) VocabularyLevel level,
        @RequestParam(required = false) Boolean premium,
        @RequestParam(required = false) String tag,
        @RequestParam(required = false) String query,
        @ParameterObject Pageable pageable
    ) {
        Page<VocabularyDtos.VocabularyWordResponse> page = vocabularyAdminService
            .listWords(status, level, premium, tag, query, pageable)
            .map(VocabularyMapper::toWordResponse);
        return ApiResponse.success(PageResponse.from(page));
    }

    @GetMapping("/words/{wordId}")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> getWord(@PathVariable UUID wordId) {
        return ApiResponse.success(VocabularyMapper.toWordResponse(vocabularyAdminService.getWord(wordId)));
    }

    @PostMapping("/words/{wordId}/examples")
    public ApiResponse<VocabularyDtos.VocabularyExampleResponse> addExample(
        @PathVariable UUID wordId,
        @Valid @RequestBody VocabularyDtos.VocabularyExampleRequest request
    ) {
        return ApiResponse.success(VocabularyMapper.toExampleResponse(vocabularyAdminService.addExample(wordId, request)));
    }

    @PutMapping("/examples/{exampleId}")
    public ApiResponse<VocabularyDtos.VocabularyExampleResponse> updateExample(
        @PathVariable UUID exampleId,
        @Valid @RequestBody VocabularyDtos.VocabularyExampleRequest request
    ) {
        return ApiResponse.success(VocabularyMapper.toExampleResponse(vocabularyAdminService.updateExample(exampleId, request)));
    }

    @DeleteMapping("/examples/{exampleId}")
    public ApiResponse<Void> deleteExample(@PathVariable UUID exampleId) {
        vocabularyAdminService.deleteExample(exampleId);
        return ApiResponse.success(null);
    }

    @PostMapping("/ai/enrich")
    public ApiResponse<VocabularyDtos.AiEnrichmentResponse> enrichWithAi(
        @Valid @RequestBody VocabularyDtos.AiEnrichmentRequest request
    ) {
        return ApiResponse.success(vocabularyAiEnrichmentService.enrich(request));
    }
}
