package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.application.progress.ProgressEnums.ModuleType;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingSkillStatus;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.reading.UserReadingSkillProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.UserVocabularyProgressRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeakAreaAggregationService {
    private final UserReadingSkillProgressRepository userReadingSkillProgressRepository;
    private final UserGrammarProfileRepository userGrammarProfileRepository;
    private final UserVocabularyProgressRepository userVocabularyProgressRepository;

    public List<WeakArea> aggregate(Long userId) {
        List<WeakArea> result = new ArrayList<>();

        userReadingSkillProgressRepository.findByUserIdAndStatusIn(userId, List.of(ReadingSkillStatus.WEAK))
            .stream()
            .sorted((a, b) -> Double.compare(b.getWeakScore(), a.getWeakScore()))
            .limit(3)
            .forEach(progress -> result.add(new WeakArea(
                ModuleType.READING,
                progress.getSkillType().name(),
                progress.getSkillKey(),
                progress.getStatus().name()
            )));

        userGrammarProfileRepository.findByUserId(userId).ifPresent(profile -> {
            profile.getWeakTopics().stream().limit(2)
                .forEach(topic -> result.add(new WeakArea(ModuleType.GRAMMAR, "TOPIC", topic, "WEAK")));
        });

        long difficultWords = userVocabularyProgressRepository.countByUserIdAndMarkedDifficultTrue(userId);
        if (difficultWords > 0) {
            result.add(new WeakArea(ModuleType.VOCABULARY, "DIFFICULT_WORDS", String.valueOf(difficultWords), "WEAK"));
        }

        return result.stream().limit(6).toList();
    }
}
