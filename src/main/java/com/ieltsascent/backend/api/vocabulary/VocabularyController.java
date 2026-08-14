package com.ieltsascent.backend.api.vocabulary;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.ApiResponseConstant;
import com.ieltsascent.backend.api.common.ApiResponseUtil;
import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.vocabulary.VocabularyApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {
    private final VocabularyApiService vocabularyApiService;

    @GetMapping("/recommendations")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyRecommendationResponse>> recommendations(
        @RequestParam(defaultValue = "20") int limit
    ) {
        return ApiResponseUtil.success(vocabularyApiService.recommendations(limit), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/words/{wordId}")
    public ResponseEntity<ApiResponse<VocabularyDtos.VocabularyWordResponse>> wordDetails(@PathVariable Long wordId) {
        return ApiResponseUtil.success(vocabularyApiService.wordDetails(wordId), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/practice-session")
    public ResponseEntity<ApiResponse<VocabularyDtos.PracticeSessionResponse>> practiceSession(
        @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponseUtil.success(vocabularyApiService.practiceSession(size), ApiResponseConstant.SUCCESS);
    }

    @PostMapping("/practice-attempts")
    public ResponseEntity<ApiResponse<VocabularyDtos.PracticeSubmitResponse>> submitPractice(
        @Valid @RequestBody VocabularyDtos.SubmitPracticeResultRequest request
    ) {
        return ApiResponseUtil.success(vocabularyApiService.submitPractice(request), ApiResponseConstant.SUCCESS);
    }

    @PatchMapping("/words/{wordId}/difficult")
    public ResponseEntity<ApiResponse<Void>> markDifficult(
        @PathVariable Long wordId,
        @Valid @RequestBody VocabularyDtos.MarkDifficultRequest request
    ) {
        vocabularyApiService.markDifficult(wordId, request.difficult());
        return ApiResponseUtil.success(null, ApiResponseConstant.UPDATED);
    }

    @GetMapping("/progress-summary")
    public ResponseEntity<ApiResponse<VocabularyDtos.ProgressSummaryResponse>> progressSummary() {
        return ApiResponseUtil.success(vocabularyApiService.progressSummary(), ApiResponseConstant.SUCCESS);
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<VocabularyDtos.UserLanguageProfileResponse>> getProfile() {
        return ApiResponseUtil.success(vocabularyApiService.profile(), ApiResponseConstant.SUCCESS);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<VocabularyDtos.UserLanguageProfileResponse>> upsertProfile(
        @Valid @RequestBody VocabularyDtos.UserLanguageProfileUpsertRequest request
    ) {
        return ApiResponseUtil.success(vocabularyApiService.upsertProfile(request), ApiResponseConstant.UPDATED);
    }
}
