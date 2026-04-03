package com.ieltsascent.backend.api.grammar.dto;

import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarDifficulty;
import com.ieltsascent.backend.domain.grammar.GrammarLevel;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class GrammarDtos {
    private GrammarDtos() {}

    public record TopicUpsertRequest(
        @NotBlank String title,
        @Size(max = 4000) String description,
        String icon,
        @NotNull Integer orderIndex,
        @NotNull GrammarLevel level,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMin,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMax,
        @NotNull Boolean premium,
        @NotNull Boolean unlockedByDefault,
        @NotNull GrammarContentStatus status,
        UUID prerequisiteTopicId
    ) {}

    public record LessonUpsertRequest(
        @NotNull UUID topicId,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull Integer orderIndex,
        @NotNull GrammarDifficulty difficulty,
        @NotNull Boolean premium,
        @NotNull GrammarContentStatus status,
        @NotNull Boolean completedByDefault
    ) {}

    public record LessonExampleUpsertRequest(
        @NotNull UUID lessonId,
        @NotBlank String correctSentence,
        String incorrectSentence,
        @NotBlank String explanation,
        @NotNull Boolean aiGenerated,
        @NotNull Boolean approved
    ) {}

    public record QuestionUpsertRequest(
        @NotNull UUID topicId,
        UUID lessonId,
        @NotNull GrammarQuestionType type,
        @NotBlank String question,
        Set<@NotBlank String> options,
        @NotBlank String correctAnswer,
        String explanation,
        @NotNull GrammarDifficulty difficulty,
        @NotNull Boolean premium,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMin,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMax,
        Set<@NotBlank String> tags,
        @NotNull GrammarContentStatus status,
        @NotNull Boolean aiGenerated,
        @NotNull Boolean reviewedByAdmin
    ) {}

    public record StatusUpdateRequest(@NotNull GrammarContentStatus status) {}

    public record TopicResponse(
        UUID id, String title, String description, String icon, Integer orderIndex, GrammarLevel level,
        Double ieltsBandMin, Double ieltsBandMax, Boolean premium, Boolean unlockedByDefault,
        GrammarContentStatus status, UUID prerequisiteTopicId, Instant createdAt, Instant updatedAt
    ) {}

    public record LessonResponse(
        UUID id, UUID topicId, String topicTitle, String title, String content, Integer orderIndex,
        GrammarDifficulty difficulty, Boolean premium, GrammarContentStatus status, Boolean completedByDefault,
        List<LessonExampleResponse> examples, Instant createdAt, Instant updatedAt
    ) {}

    public record LessonExampleResponse(
        UUID id, UUID lessonId, String correctSentence, String incorrectSentence, String explanation,
        Boolean aiGenerated, Boolean approved, Instant createdAt, Instant updatedAt
    ) {}

    public record QuestionResponse(
        UUID id, UUID topicId, UUID lessonId, GrammarQuestionType type, String question, Set<String> options,
        String correctAnswer, String explanation, GrammarDifficulty difficulty, Boolean premium,
        Double ieltsBandMin, Double ieltsBandMax, Set<String> tags, GrammarContentStatus status,
        Boolean aiGenerated, Boolean reviewedByAdmin, Instant createdAt, Instant updatedAt
    ) {}

    public record DrillTemplateResponse(
        UUID id, String name, String icon, Integer estimatedTimeMinutes, Integer questionCount,
        GrammarQuestionType type, Boolean active
    ) {}

    public record UserGrammarProfileUpsertRequest(
        @NotNull EnglishLevel englishLevel,
        @DecimalMin("0.0") @DecimalMax("9.0") Double currentIeltsBand,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double targetIeltsBand,
        Set<@NotBlank String> weakTopics,
        Set<@NotNull GrammarQuestionType> weakDrillTypes,
        @NotNull Boolean premiumUser
    ) {}

    public record UserGrammarProfileResponse(
        UUID userId, EnglishLevel englishLevel, Double currentIeltsBand, Double targetIeltsBand,
        Set<String> weakTopics, Set<GrammarQuestionType> weakDrillTypes, Boolean premiumUser
    ) {}

    public record ProgressTopicItem(
        UUID topicId, String topicTitle, Integer completedLessons, Integer totalLessons,
        Double masteryScore, Instant lastPracticedAt, Boolean unlocked, Boolean completed
    ) {}

    public record ProgressSummaryResponse(List<ProgressTopicItem> topics, Double overallMastery) {}

    public record CompleteLessonRequest(@NotNull UUID lessonId) {}

    public record SubmitPracticeRequest(@NotEmpty List<@Valid PracticeAnswerItem> answers) {}

    public record PracticeAnswerItem(@NotNull UUID questionId, String userAnswer) {}

    public record SubmitPracticeResponse(
        int total,
        int correct,
        int wrong,
        Set<String> weakTopics,
        Set<GrammarQuestionType> weakDrillTypes,
        List<WrongAnswerFeedback> feedback
    ) {}

    public record WrongAnswerFeedback(UUID questionId, String explanation, String aiFeedback) {}

    public record RecommendationTopicItem(UUID topicId, String title, String reason, double score) {}

    public record RecommendationQuestionItem(UUID questionId, String question, GrammarQuestionType type, String reason, double score) {}

    public record RecommendationResponse(List<RecommendationTopicItem> topics, List<RecommendationQuestionItem> practice) {}

    public record AiLessonEnrichmentRequest(
        @NotNull UUID topicId,
        @NotBlank String topicTitle,
        @NotBlank String lessonTitle,
        @NotBlank String lessonContent,
        @NotNull EnglishLevel englishLevel,
        @DecimalMin("0.0") @DecimalMax("9.0") Double currentBand,
        @DecimalMin("0.0") @DecimalMax("9.0") Double targetBand
    ) {}

    public record AiQuestionFeedbackRequest(
        @NotBlank String question,
        @NotBlank String correctAnswer,
        String userAnswer,
        @NotBlank String topicTitle,
        @NotNull GrammarQuestionType questionType,
        @NotNull EnglishLevel englishLevel
    ) {}

    public record AiLessonEnrichmentResponse(
        String simpleExplanation,
        List<String> correctExamples,
        String incorrectExample,
        String correction,
        String ieltsStyleExample,
        String learningHint
    ) {}

    public record AiQuestionFeedbackResponse(String feedback, String hint) {}
}
