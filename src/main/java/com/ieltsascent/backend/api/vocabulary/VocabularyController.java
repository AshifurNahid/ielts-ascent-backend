package com.ieltsascent.backend.api.vocabulary;

import com.ieltsascent.backend.api.common.ApiResponse;
import com.ieltsascent.backend.api.common.SecurityUtils;
import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.vocabulary.UserLanguageProfileService;
import com.ieltsascent.backend.application.vocabulary.VocabularyMapper;
import com.ieltsascent.backend.application.vocabulary.VocabularyPracticeService;
import com.ieltsascent.backend.application.vocabulary.VocabularyUserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private final VocabularyUserService vocabularyUserService;
    private final VocabularyPracticeService vocabularyPracticeService;
    private final UserLanguageProfileService userLanguageProfileService;

    @GetMapping("/recommendations")
    public ApiResponse<VocabularyDtos.VocabularyRecommendationResponse> recommendations(
        Authentication authentication,
        @RequestParam(defaultValue = "20") int limit
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        List<VocabularyDtos.VocabularyWordResponse> words = vocabularyUserService.getRecommendations(userId, Math.min(limit, 50))
            .stream()
            .map(VocabularyMapper::toWordResponse)
            .toList();
        return ApiResponse.success(new VocabularyDtos.VocabularyRecommendationResponse(words));
    }

    @GetMapping("/words/{wordId}")
    public ApiResponse<VocabularyDtos.VocabularyWordResponse> wordDetails(
        Authentication authentication,
        @PathVariable UUID wordId
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(VocabularyMapper.toWordResponse(vocabularyUserService.getWordForUser(userId, wordId)));
    }

    @GetMapping("/practice-session")
    public ApiResponse<VocabularyDtos.PracticeSessionResponse> practiceSession(
        Authentication authentication,
        @RequestParam(defaultValue = "10") int size
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        var recommendations = vocabularyUserService.getRecommendations(userId, Math.min(size, 30));
        return ApiResponse.success(vocabularyPracticeService.buildPracticeSession(recommendations));
    }

    @PostMapping("/practice-attempts")
    public ApiResponse<VocabularyDtos.PracticeSubmitResponse> submitPractice(
        Authentication authentication,
        @Valid @RequestBody VocabularyDtos.SubmitPracticeResultRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(vocabularyPracticeService.submit(userId, request));
    }

    @PatchMapping("/words/{wordId}/difficult")
    public ApiResponse<Void> markDifficult(
        Authentication authentication,
        @PathVariable UUID wordId,
        @Valid @RequestBody VocabularyDtos.MarkDifficultRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        vocabularyPracticeService.markDifficult(userId, wordId, request.difficult());
        return ApiResponse.success(null);
    }

    @GetMapping("/progress-summary")
    public ApiResponse<VocabularyDtos.ProgressSummaryResponse> progressSummary(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(vocabularyPracticeService.getSummary(userId));
    }

    @GetMapping("/profile")
    public ApiResponse<VocabularyDtos.UserLanguageProfileResponse> getProfile(Authentication authentication) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(VocabularyMapper.toLanguageProfileResponse(userLanguageProfileService.getOrCreate(userId)));
    }

    @PutMapping("/profile")
    public ApiResponse<VocabularyDtos.UserLanguageProfileResponse> upsertProfile(
        Authentication authentication,
        @Valid @RequestBody VocabularyDtos.UserLanguageProfileUpsertRequest request
    ) {
        UUID userId = SecurityUtils.currentUserId(authentication);
        return ApiResponse.success(VocabularyMapper.toLanguageProfileResponse(userLanguageProfileService.upsert(userId, request)));
    }
}
