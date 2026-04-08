package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.application.progress.ProgressEnums.ConfidenceLevel;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.UserVocabularyProgressRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PredictionService {
    private final UserGrammarProfileRepository userGrammarProfileRepository;
    private final UserVocabularyProgressRepository userVocabularyProgressRepository;

    public Prediction compute(Long userId, Map<SkillType, SkillEstimate> estimates) {
        double listening = fallback(estimates.get(SkillType.LISTENING));
        double reading = fallback(estimates.get(SkillType.READING));
        double writing = fallback(estimates.get(SkillType.WRITING));
        double speaking = fallback(estimates.get(SkillType.SPEAKING));

        double overall = ProgressMath.roundBand((listening + reading + writing + speaking) / 4d);
        ConfidenceLevel confidence = overallConfidence(estimates);
        List<String> keyFactors = factors(userId, estimates);

        return new Prediction(
            overall,
            listening,
            reading,
            writing,
            speaking,
            confidence,
            null,
            null,
            keyFactors
        );
    }

    private double fallback(SkillEstimate estimate) {
        return estimate != null && estimate.currentBand() != null ? estimate.currentBand() : 5.0;
    }

    private ConfidenceLevel overallConfidence(Map<SkillType, SkillEstimate> estimates) {
        long high = estimates.values().stream().filter(s -> s.confidenceLevel() == ConfidenceLevel.HIGH).count();
        long mediumOrHigh = estimates.values().stream().filter(s -> s.confidenceLevel() != ConfidenceLevel.LOW).count();
        if (high == 4) {
            return ConfidenceLevel.HIGH;
        }
        if (mediumOrHigh >= 2) {
            return ConfidenceLevel.MEDIUM;
        }
        return ConfidenceLevel.LOW;
    }

    private List<String> factors(Long userId, Map<SkillType, SkillEstimate> estimates) {
        List<String> factors = new ArrayList<>();
        estimates.values().forEach(skill -> {
            if (skill.currentBand() == null) {
                factors.add("limited " + skill.skillType().name().toLowerCase() + " evidence");
            } else if (skill.stale()) {
                factors.add(skill.skillType().name().toLowerCase() + " data is stale");
            } else {
                factors.add("recent " + skill.skillType().name().toLowerCase() + " performance available");
            }
        });

        UserGrammarProfile grammarProfile = userGrammarProfileRepository.findByUserId(userId).orElse(null);
        if (grammarProfile != null && grammarProfile.getCurrentIeltsBand() != null) {
            factors.add("grammar profile updated");
        }

        long vocabRecent = userVocabularyProgressRepository.countByUserIdAndLastPracticedAtAfter(
            userId, Instant.now().minus(14, ChronoUnit.DAYS));
        if (vocabRecent > 0) {
            factors.add("consistent vocabulary activity");
        }
        return factors.stream().limit(5).toList();
    }
}
