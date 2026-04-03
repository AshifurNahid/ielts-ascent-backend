package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import com.ieltsascent.backend.domain.vocabulary.UserVocabularyProgress;
import com.ieltsascent.backend.domain.vocabulary.VocabularyLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.UserVocabularyProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyWordRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyRecommendationService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final UserVocabularyProgressRepository userVocabularyProgressRepository;

    @Transactional(readOnly = true)
    public List<VocabularyWord> recommend(UUID userId, UserLanguageProfile profile, int limit) {
        List<VocabularyWord> candidates = vocabularyWordRepository.findCandidates(
            profile.getPremiumUser(),
            levelMix(profile.getEnglishLevel()),
            profile.getCurrentIeltsBand(),
            profile.getTargetIeltsBand()
        );

        if (candidates.isEmpty()) {
            return List.of();
        }

        Map<UUID, UserVocabularyProgress> progressMap = progressMap(userId, candidates);
        return candidates.stream()
            .sorted(Comparator.comparingDouble((VocabularyWord word) -> score(word, profile, progressMap.get(word.getId()))).reversed())
            .limit(limit)
            .toList();
    }

    private Map<UUID, UserVocabularyProgress> progressMap(UUID userId, List<VocabularyWord> words) {
        List<UserVocabularyProgress> progressList = userVocabularyProgressRepository.findByUserIdAndWordIds(
            userId,
            words.stream().map(VocabularyWord::getId).toList()
        );
        Map<UUID, UserVocabularyProgress> map = new HashMap<>();
        progressList.forEach(progress -> map.put(progress.getVocabularyWord().getId(), progress));
        return map;
    }

    private double score(VocabularyWord word, UserLanguageProfile profile, UserVocabularyProgress progress) {
        double score = 0;

        score += switch (profile.getEnglishLevel()) {
            case BEGINNER -> word.getLevel() == VocabularyLevel.BEGINNER ? 40 : word.getLevel() == VocabularyLevel.INTERMEDIATE ? 8 : 2;
            case INTERMEDIATE -> word.getLevel() == VocabularyLevel.INTERMEDIATE ? 35 : word.getLevel() == VocabularyLevel.BEGINNER ? 18 : 10;
            case ADVANCED -> word.getLevel() == VocabularyLevel.ADVANCED ? 40 : 16;
        };

        boolean matchesWeakTag = word.getTags().stream().anyMatch(profile.getWeakTags()::contains);
        if (matchesWeakTag) {
            score += 20;
        }

        if (profile.getTargetIeltsBand() != null && profile.getTargetIeltsBand() >= 7.0 && word.getLevel() == VocabularyLevel.ADVANCED) {
            score += 15;
        }

        if (progress == null) {
            return score + 12;
        }

        score += Math.min(progress.getWrongCount() * 3.0, 20.0);
        score += Boolean.TRUE.equals(progress.getMarkedDifficult()) ? 12 : 0;
        score -= Math.min(progress.getMasteryLevel() * 20.0, 18.0);
        score -= recentlyPracticedPenalty(progress.getLastPracticedAt());
        return score;
    }

    private double recentlyPracticedPenalty(Instant lastPracticedAt) {
        if (lastPracticedAt == null) {
            return 0;
        }
        long days = ChronoUnit.DAYS.between(lastPracticedAt, Instant.now());
        if (days <= 1) {
            return 10;
        }
        return days <= 3 ? 4 : 0;
    }

    private Collection<VocabularyLevel> levelMix(EnglishLevel level) {
        return switch (level) {
            case BEGINNER -> List.of(VocabularyLevel.BEGINNER, VocabularyLevel.INTERMEDIATE);
            case INTERMEDIATE -> List.of(VocabularyLevel.INTERMEDIATE, VocabularyLevel.BEGINNER, VocabularyLevel.ADVANCED);
            case ADVANCED -> List.of(VocabularyLevel.ADVANCED, VocabularyLevel.INTERMEDIATE);
        };
    }
}
