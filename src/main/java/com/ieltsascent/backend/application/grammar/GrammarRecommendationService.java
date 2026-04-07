package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLevel;
import com.ieltsascent.backend.domain.grammar.GrammarPracticeAttempt;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import com.ieltsascent.backend.domain.grammar.GrammarTopic;
import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import com.ieltsascent.backend.domain.grammar.UserGrammarProgress;
import com.ieltsascent.backend.infrastructure.persistence.GrammarTopicRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarPracticeAttemptRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarQuestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProgressRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrammarRecommendationService {
    private final GrammarTopicRepository topicRepository;
    private final GrammarQuestionRepository questionRepository;
    private final GrammarPracticeAttemptRepository attemptRepository;
    private final UserGrammarProgressRepository progressRepository;

    @Transactional(readOnly = true)
    public GrammarDtos.RecommendationResponse recommend(Long userId, UserGrammarProfile profile, int limit) {
        List<GrammarTopic> topics = topicRepository.findAllByStatusOrderByOrderIndexAsc(GrammarContentStatus.PUBLISHED);
        List<UserGrammarProgress> progressList = progressRepository.findByUserId(userId);
        var progressMap = progressList.stream().collect(java.util.stream.Collectors.toMap(p -> p.getTopic().getId(), p -> p));
        Set<String> weakTopics = profile.getWeakTopics();
        Map<GrammarQuestionType, Long> wrongByType = attemptRepository.findTop200ByUserIdOrderByCreatedAtDesc(userId).stream()
            .filter(it -> !Boolean.TRUE.equals(it.getCorrect()))
            .collect(java.util.stream.Collectors.groupingBy(GrammarPracticeAttempt::getQuestionType, java.util.stream.Collectors.counting()));

        List<GrammarDtos.RecommendationTopicItem> topicItems = new ArrayList<>();
        for (GrammarTopic topic : topics) {
            if (!profile.getPremiumUser() && topic.getPremium()) continue;
            double score = 0;
            if (matchesLevel(profile, topic)) score += 3.0;
            if (weakTopics.contains(topic.getTitle().trim().toLowerCase())) score += 4.0;
            if (topic.getIeltsBandMax() <= profile.getTargetIeltsBand() + 0.5) score += 2.0;
            UserGrammarProgress p = progressMap.get(topic.getId());
            if (p != null) score += Math.max(0, 2.5 - p.getMasteryScore() / 40.0);
            String reason = weakTopics.contains(topic.getTitle().trim().toLowerCase()) ? "Weak topic recovery" : "Level + IELTS alignment";
            topicItems.add(new GrammarDtos.RecommendationTopicItem(topic.getId(), topic.getTitle(), reason, score));
        }
        topicItems = topicItems.stream().sorted(Comparator.comparingDouble(GrammarDtos.RecommendationTopicItem::score).reversed()).limit(limit).toList();

        List<GrammarDtos.RecommendationQuestionItem> questionItems = questionRepository.findAll().stream()
            .filter(q -> q.getStatus() == GrammarContentStatus.PUBLISHED)
            .filter(q -> profile.getPremiumUser() || !q.getPremium())
            .map(q -> {
                double score = 1.0;
                if (wrongByType.getOrDefault(q.getType(), 0L) > 2) score += 3.0;
                if (profile.getTargetIeltsBand() >= 7.0 && (q.getType() == GrammarQuestionType.TRANSFORMATION || q.getType() == GrammarQuestionType.ERROR_CORRECTION)) score += 2.0;
                if (q.getIeltsBandMax() <= profile.getTargetIeltsBand() + 0.5) score += 1.0;
                return new GrammarDtos.RecommendationQuestionItem(q.getId(), q.getQuestion(), q.getType(), "Weak drill and band-fit", score);
            })
            .sorted(Comparator.comparingDouble(GrammarDtos.RecommendationQuestionItem::score).reversed())
            .limit(limit)
            .toList();

        return new GrammarDtos.RecommendationResponse(topicItems, questionItems);
    }

    private boolean matchesLevel(UserGrammarProfile profile, GrammarTopic topic) {
        return switch (profile.getEnglishLevel()) {
            case BEGINNER -> topic.getLevel() == GrammarLevel.BEGINNER;
            case INTERMEDIATE -> topic.getLevel() != GrammarLevel.ADVANCED || profile.getWeakTopics().contains("tenses");
            case ADVANCED -> true;
        };
    }
}
