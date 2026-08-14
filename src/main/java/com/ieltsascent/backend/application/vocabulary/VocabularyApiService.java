package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyApiService {
    private final VocabularyUserService vocabularyUserService;
    private final VocabularyPracticeService vocabularyPracticeService;
    private final UserLanguageProfileService userLanguageProfileService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public VocabularyDtos.VocabularyRecommendationResponse recommendations(Long userId, int limit) {
        List<VocabularyDtos.VocabularyWordResponse> words = vocabularyUserService.getRecommendations(userId, Math.min(limit, 50))
            .stream()
            .map(VocabularyMapper::toWordResponse)
            .toList();
        return new VocabularyDtos.VocabularyRecommendationResponse(words);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.VocabularyRecommendationResponse recommendations(int limit) {
        return recommendations(currentUserProvider.userId(), limit);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.VocabularyWordResponse wordDetails(Long userId, Long wordId) {
        return VocabularyMapper.toWordResponse(vocabularyUserService.getWordForUser(userId, wordId));
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.VocabularyWordResponse wordDetails(Long wordId) {
        return wordDetails(currentUserProvider.userId(), wordId);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.PracticeSessionResponse practiceSession(Long userId, int size) {
        return vocabularyPracticeService.buildPracticeSession(vocabularyUserService.getRecommendations(userId, Math.min(size, 30)));
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.PracticeSessionResponse practiceSession(int size) {
        return practiceSession(currentUserProvider.userId(), size);
    }

    @Transactional
    public VocabularyDtos.PracticeSubmitResponse submitPractice(Long userId, VocabularyDtos.SubmitPracticeResultRequest request) {
        return vocabularyPracticeService.submit(userId, request);
    }

    @Transactional
    public VocabularyDtos.PracticeSubmitResponse submitPractice(VocabularyDtos.SubmitPracticeResultRequest request) {
        return submitPractice(currentUserProvider.userId(), request);
    }

    @Transactional
    public void markDifficult(Long userId, Long wordId, boolean difficult) {
        vocabularyPracticeService.markDifficult(userId, wordId, difficult);
    }

    @Transactional
    public void markDifficult(Long wordId, boolean difficult) {
        markDifficult(currentUserProvider.userId(), wordId, difficult);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.ProgressSummaryResponse progressSummary(Long userId) {
        return vocabularyPracticeService.getSummary(userId);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.ProgressSummaryResponse progressSummary() {
        return progressSummary(currentUserProvider.userId());
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.UserLanguageProfileResponse profile(Long userId) {
        return VocabularyMapper.toLanguageProfileResponse(userLanguageProfileService.getOrCreate(userId));
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.UserLanguageProfileResponse profile() {
        return profile(currentUserProvider.userId());
    }

    @Transactional
    public VocabularyDtos.UserLanguageProfileResponse upsertProfile(Long userId, VocabularyDtos.UserLanguageProfileUpsertRequest request) {
        return VocabularyMapper.toLanguageProfileResponse(userLanguageProfileService.upsert(userId, request));
    }

    @Transactional
    public VocabularyDtos.UserLanguageProfileResponse upsertProfile(VocabularyDtos.UserLanguageProfileUpsertRequest request) {
        return upsertProfile(currentUserProvider.userId(), request);
    }
}

