package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.domain.readingtest.*;
import com.ieltsascent.backend.infrastructure.persistence.reading.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingRecommendationService {
    private final ReadingPassageRepository passageRepository;
    private final UserReadingSkillProgressRepository progressRepository;
    private final ReadingRecommendationLogRepository recommendationLogRepository;

    @Transactional
    public ReadingDtos.RecommendationResponse recommend(UserReadingProfile profile) {
        List<ReadingPassage> candidates = passageRepository.findEligibleForUser(profile.getPremiumUser(), profile.getCurrentIeltsBand(), profile.getTargetIeltsBand());
        List<UserReadingSkillProgress> skills = progressRepository.findByUserIdOrderByWeakScoreDesc(profile.getUserId());

        Set<String> weakTopics = skills.stream().filter(s -> s.getSkillType() == ReadingSkillType.TOPIC && s.getStatus() == ReadingSkillStatus.WEAK)
            .map(UserReadingSkillProgress::getSkillKey).collect(java.util.stream.Collectors.toSet());
        Set<String> weakDifficulty = skills.stream().filter(s -> s.getSkillType() == ReadingSkillType.DIFFICULTY && s.getStatus() != ReadingSkillStatus.MASTERED)
            .map(UserReadingSkillProgress::getSkillKey).collect(java.util.stream.Collectors.toSet());
        List<ReadingQuestionKind> weakTypes = skills.stream()
            .filter(s -> s.getSkillType() == ReadingSkillType.QUESTION_TYPE && s.getStatus() != ReadingSkillStatus.MASTERED)
            .map(UserReadingSkillProgress::getSkillKey)
            .map(this::toQuestionKind)
            .filter(Objects::nonNull)
            .distinct()
            .limit(4)
            .toList();

        List<ReadingPassage> ranked = candidates.stream().sorted(Comparator.comparingDouble(p -> -scorePassage(p, weakTopics, weakDifficulty, profile))).limit(10).toList();

        ReadingRecommendationLog log = new ReadingRecommendationLog();
        log.setUserId(profile.getUserId());
        log.setRecommendationType("READING_PRACTICE");
        log.setReason("Boosted weak topics=" + weakTopics + ", weak difficulty=" + weakDifficulty + ", weak types=" + weakTypes + ", targetBand=" + profile.getTargetIeltsBand());
        recommendationLogRepository.save(log);

        List<String> reasons = List.of(
            "Prioritized weak topics and weak question types.",
            "Filtered to published content and premium entitlement.",
            "Balanced current band " + profile.getCurrentIeltsBand() + " to target band " + profile.getTargetIeltsBand()
        );

        return new ReadingDtos.RecommendationResponse(ranked.stream().map(ReadingMapper::toPassageResponse).toList(), weakTypes, reasons);
    }

    private ReadingQuestionKind toQuestionKind(String value) {
        try {
            return ReadingQuestionKind.valueOf(value);
        } catch (Exception ex) {
            return null;
        }
    }

    private double scorePassage(ReadingPassage passage, Set<String> weakTopics, Set<String> weakDifficulty, UserReadingProfile profile) {
        double score = 10;
        if (weakTopics.contains(passage.getTopicTag().toUpperCase(Locale.ROOT))) score += 20;
        if (weakDifficulty.contains(passage.getDifficulty().name() + "_PASSAGES")) score += 10;
        if (passage.getIeltsBandMax() <= profile.getTargetIeltsBand() + 0.5) score += 8;
        if (passage.getIeltsBandMin() >= profile.getCurrentIeltsBand() - 0.5) score += 7;
        return score;
    }
}
