package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyWordRepository;
import com.ieltsascent.backend.domain.vocabulary.VocabularyStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyUserService {
    private final UserLanguageProfileService userLanguageProfileService;
    private final VocabularyRecommendationService recommendationService;
    private final VocabularyWordRepository vocabularyWordRepository;

    @Transactional(readOnly = true)
    public List<VocabularyWord> getRecommendations(Long userId, int limit) {
        UserLanguageProfile profile = userLanguageProfileService.getOrCreate(userId);
        return recommendationService.recommend(userId, profile, limit);
    }

    @Transactional(readOnly = true)
    public VocabularyWord getWordForUser(Long userId, Long wordId) {
        UserLanguageProfile profile = userLanguageProfileService.getOrCreate(userId);
        VocabularyWord word = vocabularyWordRepository.findDetailedById(wordId)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));

        if (word.getStatus() != VocabularyStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Vocabulary word not found");
        }
        if (!profile.getPremiumUser() && Boolean.TRUE.equals(word.getPremium())) {
            throw new ResourceNotFoundException("Vocabulary word not found");
        }
        return word;
    }
}
