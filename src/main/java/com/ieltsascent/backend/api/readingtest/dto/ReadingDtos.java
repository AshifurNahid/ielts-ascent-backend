package com.ieltsascent.backend.api.readingtest.dto;

import com.ieltsascent.backend.domain.readingtest.enums.*;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public final class ReadingDtos {
    private ReadingDtos() {}

    public record PassageUpsertRequest(
        @NotBlank String title,
        @NotBlank String content,
        String shortDescription,
        @NotBlank String topicTag,
        @NotNull ReadingDifficulty difficulty,
        @NotNull @DecimalMin("1.0") @DecimalMax("9.0") Double ieltsBandMin,
        @NotNull @DecimalMin("1.0") @DecimalMax("9.0") Double ieltsBandMax,
        @NotNull @Min(1) Integer estimatedReadingMinutes,
        @NotNull Boolean premium,
        @NotNull ReadingContentStatus status
    ) {}

    public record QuestionUpsertRequest(
        @NotNull Long passageId,
        Integer groupNumber,
        @NotNull ReadingQuestionKind type,
        @NotBlank String prompt,
        List<@NotBlank String> options,
        @NotBlank String correctAnswer,
        String explanation,
        String answerSourceHint,
        @NotNull ReadingDifficulty difficulty,
        @NotNull @Min(1) Integer orderIndex,
        @NotNull Boolean premium,
        @NotNull ReadingContentStatus status,
        List<@NotBlank String> tags
    ) {}

    public record TestUpsertRequest(
        @NotBlank String title,
        String description,
        @NotNull @Min(1) Integer totalTimeMinutes,
        @NotNull ReadingDifficulty difficulty,
        @NotNull Boolean premium,
        @NotNull ReadingContentStatus status
    ) {}

    public record StatusUpdateRequest(@NotNull ReadingContentStatus status) {}

    public record PassageFilterRequest(ReadingContentStatus status, ReadingDifficulty difficulty, Boolean premium, String topicTag, String query) {}

    public record QuestionFilterRequest(ReadingContentStatus status, ReadingQuestionKind type, Long passageId, Boolean premium, String query) {}

    public record TestFilterRequest(ReadingContentStatus status, ReadingDifficulty difficulty, Boolean premium, String query) {}

    public record QuestionsByTypeRequest(@NotNull ReadingQuestionKind type) {}

    public record AssignPassagesRequest(@NotEmpty List<@Valid PassageAssignment> passages) {}
    public record PassageAssignment(@NotNull Long passageId, @NotNull @Min(1) Integer orderIndex) {}

    public record AttemptQuestionSubmit(@NotNull Long questionId, String userAnswer, @NotNull @Min(1) Integer timeSpentSeconds) {}
    public record AttemptSubmitRequest(
        Long readingTestId,
        Long passageId,
        @NotNull ReadingAttemptMode mode,
        @NotNull LocalDateTime startedAt,
        @NotNull LocalDateTime completedAt,
        @NotEmpty List<@Valid AttemptQuestionSubmit> answers
    ) {}

    public record ReadingPassageResponse(Long id, String title, String content, String shortDescription, String topicTag,
                                         ReadingDifficulty difficulty, Double ieltsBandMin, Double ieltsBandMax,
                                         Integer estimatedReadingMinutes, Boolean premium, ReadingContentStatus status,
                                         LocalDateTime createdAt, LocalDateTime updatedAt) {}

    public record ReadingQuestionResponse(Long id, Long passageId, Integer groupNumber, ReadingQuestionKind type,
                                          String prompt, List<String> options, String correctAnswer, String explanation,
                                          String answerSourceHint, ReadingDifficulty difficulty, Integer orderIndex,
                                          Boolean premium, ReadingContentStatus status, List<String> tags,
                                          LocalDateTime createdAt, LocalDateTime updatedAt) {}

    public record ReadingTestResponse(Long id, String title, String description, Integer totalTimeMinutes,
                                      ReadingDifficulty difficulty, Boolean premium, ReadingContentStatus status,
                                      LocalDateTime createdAt, LocalDateTime updatedAt) {}

    public record ReadingTestDetailResponse(ReadingTestResponse test, List<TestPassagePayload> passages) {}
    public record TestPassagePayload(ReadingPassageResponse passage, List<ReadingQuestionResponse> questions) {}

    public record AttemptSummaryResponse(Long attemptId, Integer totalQuestions, Integer correctAnswers,
                                         Double scorePercent, Integer totalTimeSeconds, List<SkillProgressResponse> updatedSkills) {}

    public record AttemptQuestionResult(Long questionId, String userAnswer, Boolean correct, String correctAnswer, String explanation, Integer timeSpentSeconds) {}
    public record AttemptResultResponse(Long attemptId, Integer totalQuestions, Integer correctAnswers, Double scorePercent,
                                        Integer totalTimeSeconds, ReadingAttemptMode mode, Instant startedAt, Instant completedAt,
                                        List<AttemptQuestionResult> questionResults) {}

    public record SkillProgressResponse(Long id, ReadingSkillType skillType, String skillKey, Integer totalAttempts,
                                        Integer totalCorrect, Integer recentAttempts, Integer recentCorrect,
                                        Double averageTimeSeconds, Double masteryScore, Double weakScore,
                                        ReadingSkillStatus status, Instant lastPracticedAt, Instant lastImprovedAt) {}

    public record RecommendationResponse(List<ReadingPassageResponse> passages,
                                         List<ReadingQuestionKind> recommendedQuestionTypes,
                                         List<String> reasons) {}

    public record ReadingAiExplainRequest(@NotNull Long questionId, String userAnswer) {}
    public record ReadingAiExplainResponse(String correctAnswerExplanation, String wrongAnswerReason, String strategyTip) {}

    public record AdminAiEnrichRequest(@NotBlank String passageContent, @NotBlank String questionPrompt,
                                       @NotBlank String correctAnswer, @NotNull ReadingQuestionKind questionType) {}
    public record AdminAiEnrichResponse(String draftExplanation, String answerSourceHint, String learnerTip, List<String> draftDistractors, String shortSummary) {}

    public record ReadingProfileUpsertRequest(@NotNull EnglishLevel englishLevel,
                                              @NotNull @DecimalMin("1.0") @DecimalMax("9.0") Double currentIeltsBand,
                                              @NotNull @DecimalMin("1.0") @DecimalMax("9.0") Double targetIeltsBand,
                                              @NotNull Boolean premiumUser) {}

    public record ReadingProfileResponse(EnglishLevel englishLevel,
                                         Double currentIeltsBand,
                                         Double targetIeltsBand,
                                         Boolean premiumUser) {}
}
