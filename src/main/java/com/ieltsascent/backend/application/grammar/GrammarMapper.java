package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.domain.grammar.GrammarDrillTemplate;
import com.ieltsascent.backend.domain.grammar.GrammarLesson;
import com.ieltsascent.backend.domain.grammar.GrammarLessonExample;
import com.ieltsascent.backend.domain.grammar.GrammarPracticeAttempt;
import com.ieltsascent.backend.domain.grammar.GrammarQuestion;
import com.ieltsascent.backend.domain.grammar.GrammarTopic;
import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import com.ieltsascent.backend.domain.grammar.UserGrammarProgress;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public final class GrammarMapper {
    private GrammarMapper() {}

    public static GrammarDtos.TopicResponse toTopicResponse(GrammarTopic topic) {
        return new GrammarDtos.TopicResponse(topic.getId(), topic.getTitle(), topic.getDescription(), topic.getIcon(),
            topic.getOrderIndex(), topic.getLevel(), topic.getIeltsBandMin(), topic.getIeltsBandMax(), topic.getPremium(),
            topic.getUnlockedByDefault(), topic.getStatus(), topic.getPrerequisiteTopicId(), topic.getCreatedAt(), topic.getUpdatedAt());
    }

    public static GrammarDtos.LessonResponse toLessonResponse(GrammarLesson lesson, List<GrammarLessonExample> examples) {
        return new GrammarDtos.LessonResponse(lesson.getId(), lesson.getTopic().getId(), lesson.getTopic().getTitle(),
            lesson.getTitle(), lesson.getContent(), lesson.getOrderIndex(), lesson.getDifficulty(), lesson.getPremium(),
            lesson.getStatus(), lesson.getCompletedByDefault(), examples.stream().map(GrammarMapper::toLessonExampleResponse).toList(),
            lesson.getCreatedAt(), lesson.getUpdatedAt());
    }

    public static GrammarDtos.LessonExampleResponse toLessonExampleResponse(GrammarLessonExample example) {
        return new GrammarDtos.LessonExampleResponse(example.getId(), example.getLesson().getId(), example.getCorrectSentence(),
            example.getIncorrectSentence(), example.getExplanation(), example.getAiGenerated(), example.getApproved(),
            example.getCreatedAt(), example.getUpdatedAt());
    }

    public static GrammarDtos.QuestionResponse toQuestionResponse(GrammarQuestion question) {
        return new GrammarDtos.QuestionResponse(question.getId(), question.getTopic().getId(),
            question.getLesson() == null ? null : question.getLesson().getId(), question.getType(), question.getQuestion(),
            question.getOptions(), question.getCorrectAnswer(), question.getExplanation(), question.getDifficulty(), question.getPremium(),
            question.getIeltsBandMin(), question.getIeltsBandMax(), question.getTags(), question.getStatus(), question.getAiGenerated(),
            question.getReviewedByAdmin(), question.getCreatedAt(), question.getUpdatedAt());
    }

    public static GrammarDtos.DrillTemplateResponse toDrillTemplateResponse(GrammarDrillTemplate template) {
        return new GrammarDtos.DrillTemplateResponse(template.getId(), template.getName(), template.getIcon(),
            template.getEstimatedTimeMinutes(), template.getQuestionCount(), template.getType(), template.getActive());
    }

    public static GrammarDtos.UserGrammarProfileResponse toProfileResponse(UserGrammarProfile profile) {
        return new GrammarDtos.UserGrammarProfileResponse(profile.getUserId(), profile.getEnglishLevel(), profile.getCurrentIeltsBand(),
            profile.getTargetIeltsBand(), profile.getWeakTopics(), profile.getWeakDrillTypes(), profile.getPremiumUser());
    }

    public static GrammarDtos.ProgressSummaryResponse toProgressSummary(List<UserGrammarProgress> items) {
        List<GrammarDtos.ProgressTopicItem> topicItems = items.stream()
            .sorted(Comparator.comparing(it -> it.getTopic().getOrderIndex()))
            .map(it -> new GrammarDtos.ProgressTopicItem(it.getTopic().getId(), it.getTopic().getTitle(), it.getCompletedLessons(),
                it.getTotalLessons(), it.getMasteryScore(), it.getLastPracticedAt(), it.getUnlocked(), it.getCompleted()))
            .toList();
        double overall = items.isEmpty() ? 0.0 : items.stream().mapToDouble(UserGrammarProgress::getMasteryScore).average().orElse(0.0);
        return new GrammarDtos.ProgressSummaryResponse(topicItems, overall);
    }

    public static Set<String> normalizeStringSet(Set<String> values) {
        if (values == null) {
            return Set.of();
        }
        return values.stream().map(String::trim).filter(v -> !v.isBlank()).map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
    }

    public static GrammarPracticeAttempt toAttempt(Long userId, GrammarQuestion question, String userAnswer, boolean correct) {
        GrammarPracticeAttempt attempt = new GrammarPracticeAttempt();
        attempt.setUserId(userId);
        attempt.setGrammarQuestionId(question.getId());
        attempt.setTopicId(question.getTopic().getId());
        attempt.setLessonId(question.getLesson() == null ? null : question.getLesson().getId());
        attempt.setQuestionType(question.getType());
        attempt.setUserAnswer(userAnswer);
        attempt.setCorrect(correct);
        return attempt;
    }

}
