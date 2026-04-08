package com.ieltsascent.backend.api.vocabulary.dto;

import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExampleType;
import com.ieltsascent.backend.domain.vocabulary.VocabularyLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyPracticeQuestionType;
import com.ieltsascent.backend.domain.vocabulary.VocabularyStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class VocabularyDtos {
    private VocabularyDtos() {
    }

    public record VocabularyWordUpsertRequest(
        @NotBlank String word,
        @NotBlank String simpleMeaning,
        String detailedMeaning,
        @NotBlank String partOfSpeech,
        @NotNull VocabularyLevel level,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMin,
        @NotNull @DecimalMin("0.0") @DecimalMax("9.0") Double ieltsBandMax,
        Set<@NotBlank String> tags,
        Set<@NotBlank String> synonyms,
        Set<@NotBlank String> antonyms,
        Set<@NotBlank String> collocations,
        String commonMistake,
        @NotNull Boolean premium
    ) {
    }

    public record VocabularyExampleRequest(
        @NotNull VocabularyExampleType type,
        @NotBlank @Size(max = 2000) String sentence,
        @NotNull Boolean aiGenerated,
        @NotNull Boolean approved
    ) {
    }

    public record VocabularyStatusUpdateRequest(@NotNull VocabularyStatus status) {
    }

    public record VocabularyWordResponse(
        Long id,
        String word,
        String simpleMeaning,
        String detailedMeaning,
        String partOfSpeech,
        VocabularyLevel level,
        Double ieltsBandMin,
        Double ieltsBandMax,
        Set<String> tags,
        Set<String> synonyms,
        Set<String> antonyms,
        Set<String> collocations,
        String commonMistake,
        Boolean premium,
        VocabularyStatus status,
        List<VocabularyExampleResponse> examples,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }

    public record VocabularyExampleResponse(
        Long id,
        VocabularyExampleType type,
        String sentence,
        Boolean aiGenerated,
        Boolean approved,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }

    public record VocabularyRecommendationResponse(List<VocabularyWordResponse> words) {
    }

    public record PracticeSessionResponse(List<PracticeQuestionItem> items) {
    }

    public record PracticeQuestionItem(
        Long vocabularyWordId,
        String word,
        VocabularyPracticeQuestionType questionType,
        String prompt,
        List<String> options
    ) {
    }

    public record SubmitPracticeResultRequest(@NotEmpty List<@Valid PracticeAnswerRequest> answers) {
    }

    public record PracticeAnswerRequest(
        @NotNull Long vocabularyWordId,
        @NotNull VocabularyPracticeQuestionType questionType,
        String userAnswer,
        @NotNull Boolean correct
    ) {
    }

    public record PracticeSubmitResponse(int total, int correct, int wrong) {
    }

    public record MarkDifficultRequest(@NotNull Boolean difficult) {
    }

    public record ProgressSummaryResponse(
        long masteredWords,
        long difficultWords,
        long practicedLast7Days,
        long totalTrackedWords
    ) {
    }

    public record UserLanguageProfileUpsertRequest(
        @NotNull EnglishLevel englishLevel,
        @DecimalMin("0.0") @DecimalMax("9.0") Double currentIeltsBand,
        @DecimalMin("0.0") @DecimalMax("9.0") Double targetIeltsBand,
        Set<@NotBlank String> weakTags,
        @NotNull Boolean premiumUser
    ) {
    }

    public record UserLanguageProfileResponse(
        Long userId,
        EnglishLevel englishLevel,
        Double currentIeltsBand,
        Double targetIeltsBand,
        Set<String> weakTags,
        Boolean premiumUser
    ) {
    }

    public record AiEnrichmentRequest(
        @NotNull Long vocabularyWordId,
        @NotNull Boolean includeImprovedMeaning
    ) {
    }

    public record AiEnrichmentResponse(
        String simpleExplanation,
        List<String> writingExamples,
        String speakingExample,
        String simpleExample,
        List<String> collocations,
        String commonMistake,
        String commonMistakeExplanation
    ) {
    }
}
