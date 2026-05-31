package com.ieltsascent.backend.api.vocabulary;

import com.ieltsascent.backend.api.common.ApiResponse;
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

@RestController
@RequestMapping("/api/vocabulary")
@RequiredArgsConstructor
public class VocabularyController {
    private final VocabularyApiService vocabularyApiService;

    @GetMapping("/recommendations")
    public ApiResponse<VocabularyDtos.VocabularyRecommendationResponse> recommendations(
        @RequestParam(defaultValue = "20") int limit
    ) {
        return ApiResponse.success(vocabularyApiService.recommendations(limit));
    }

    @GetMapping("/words/{wordId}")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> wordDetails(@PathVariable Long wordId) {
        return ApiResponse.success(vocabularyApiService.wordDetails(wordId));
    }

    @GetMapping("/practice-session")
    public ApiResponse<VocabularyDtos.PracticeSessionResponse> practiceSession(
        @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(vocabularyApiService.practiceSession(size));
    }

    @PostMapping("/practice-attempts")
    public ApiResponse<VocabularyDtos.PracticeSubmitResponse> submitPractice(
        @Valid @RequestBody VocabularyDtos.SubmitPracticeResultRequest request
    ) {
        return ApiResponse.success(vocabularyApiService.submitPractice(request));
    }

    @PatchMapping("/words/{wordId}/difficult")
    public ApiResponse<Void> markDifficult(
        @PathVariable Long wordId,
        @Valid @RequestBody VocabularyDtos.MarkDifficultRequest request
    ) {
        vocabularyApiService.markDifficult(wordId, request.difficult());
        return ApiResponse.success(null);
    }

    @GetMapping("/progress-summary")
    public ApiResponse<VocabularyDtos.ProgressSummaryResponse> progressSummary() {
        return ApiResponse.success(vocabularyApiService.progressSummary());
    }

    @GetMapping("/profile")
    public ApiResponse<VocabularyDtos.UserLanguageProfileResponse> getProfile() {
        return ApiResponse.success(vocabularyApiService.profile());
    }

    @PutMapping("/profile")
    public ApiResponse<VocabularyDtos.UserLanguageProfileResponse> upsertProfile(
        @Valid @RequestBody VocabularyDtos.UserLanguageProfileUpsertRequest request
    ) {
        return ApiResponse.success(vocabularyApiService.upsertProfile(request));
    }
}
