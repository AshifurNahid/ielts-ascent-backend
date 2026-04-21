package com.ieltsascent.backend.api.writing.dto;

import com.ieltsascent.backend.domain.writing.UserWritingProgress;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingTemplate;
import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class WritingDtos {
    private WritingDtos() {
    }

    public record PromptResponse(
        Long id,
        String title,
        String taskType,
        String promptText,
        String instructions,
        List<String> tags,
        String difficulty,
        Double ieltsBandMin,
        Double ieltsBandMax,
        boolean premium,
        String status,
        boolean templateEnabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        public static PromptResponse from(WritingPrompt prompt) {
            return new PromptResponse(
                prompt.getId(), prompt.getTitle(), enumName(prompt.getTaskType()), prompt.getPromptText(), prompt.getInstructions(),
                Objects.requireNonNullElse(prompt.getTags(), List.of()), enumName(prompt.getDifficulty()), prompt.getIeltsBandMin(), prompt.getIeltsBandMax(),
                prompt.isPremium(), enumName(prompt.getStatus()), prompt.isTemplateEnabled(), prompt.getCreatedAt(), prompt.getUpdatedAt()
            );
        }
    }

    public record TemplateResponse(Long id, Long promptId, String taskType, String title, String description, String templateContent,
                                   Double targetBand, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        public static TemplateResponse from(WritingTemplate template) {
            return new TemplateResponse(
                template.getId(), template.getPrompt() == null ? null : template.getPrompt().getId(), enumName(template.getTaskType()),
                template.getTitle(), template.getDescription(), template.getTemplateContent(), template.getTargetBand(), template.isActive(),
                template.getCreatedAt(), template.getUpdatedAt()
            );
        }
    }

    public record SubmissionResponse(
        Long id,
        Long promptId,
        String taskType,
        String essayText,
        Integer wordCount,
        boolean timedMode,
        Instant startedAt,
        Instant submittedAt,
        Long durationSeconds,
        String status,
        Double overallBand,
        Double taskResponseBand,
        Double coherenceBand,
        Double lexicalBand,
        Double grammarBand,
        Double evaluationConfidence,
        String aiSummary,
        List<WeakPointResponse> weakPoints,
        List<SuggestionResponse> suggestions
    ) {
        public static SubmissionResponse from(WritingSubmission submission, List<WritingWeakPoint> weakPoints, List<WritingSuggestion> suggestions) {
            return new SubmissionResponse(
                submission.getId(),
                submission.getPrompt() == null ? null : submission.getPrompt().getId(),
                enumName(submission.getTaskType()),
                submission.getEssayText(),
                submission.getWordCount(),
                submission.isTimedMode(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                submission.getDurationSeconds(),
                enumName(submission.getStatus()),
                submission.getOverallBand(),
                submission.getTaskResponseBand(),
                submission.getCoherenceBand(),
                submission.getLexicalBand(),
                submission.getGrammarBand(),
                submission.getEvaluationConfidence(),
                submission.getAiSummary(),
                Objects.requireNonNullElse(weakPoints, List.<WritingWeakPoint>of()).stream().map(WeakPointResponse::from).toList(),
                Objects.requireNonNullElse(suggestions, List.<WritingSuggestion>of()).stream().map(SuggestionResponse::from).toList()
            );
        }
    }

    public record WeakPointResponse(Long id, String category, String weakKey, String severity, String explanation, String suggestion) {
        public static WeakPointResponse from(WritingWeakPoint weakPoint) {
            return new WeakPointResponse(
                weakPoint.getId(), enumName(weakPoint.getCategory()), weakPoint.getWeakKey(), enumName(weakPoint.getSeverity()),
                weakPoint.getExplanation(), weakPoint.getSuggestion()
            );
        }
    }

    public record SuggestionResponse(Long id, String suggestionType, String originalText, String suggestedText, String explanation) {
        public static SuggestionResponse from(WritingSuggestion suggestion) {
            return new SuggestionResponse(
                suggestion.getId(), enumName(suggestion.getSuggestionType()), suggestion.getOriginalText(), suggestion.getSuggestedText(), suggestion.getExplanation()
            );
        }
    }

    public record ProgressResponse(Long totalSubmissions, Double averageBand, Double latestBand, Double bestBand, Double trendValue,
                                   Double last30DayImprovement, Double last30DayNetImprovement, Double last30DayVolatility) {
        public static ProgressResponse from(UserWritingProgress progress) {
            return new ProgressResponse(
                progress == null ? 0L : progress.getTotalSubmissions(),
                progress == null ? null : progress.getAverageBand(),
                progress == null ? null : progress.getLatestBand(),
                progress == null ? null : progress.getBestBand(),
                progress == null ? null : progress.getTrendValue(),
                progress == null ? null : progress.getLast30DayImprovement(),
                progress == null ? null : progress.getLast30DayNetImprovement(),
                progress == null ? null : progress.getLast30DayVolatility()
            );
        }
    }

    public record StartSubmissionRequest(@NotNull Long promptId, boolean timedMode) {
    }

    public record SaveDraftRequest(@NotBlank String essayText) {
    }

    public record SubmitWritingRequest(@NotBlank String essayText) {
    }

    public record ImproveSectionRequest(@NotBlank String sectionText, @NotBlank String instruction) {
    }

    public record AdminPromptRequest(
        @NotBlank String title,
        @NotNull String taskType,
        @NotBlank String promptText,
        String instructions,
        List<String> tags,
        @NotNull String difficulty,
        @NotNull Double ieltsBandMin,
        @NotNull Double ieltsBandMax,
        boolean premium,
        @NotNull String status,
        boolean templateEnabled
    ) {
    }

    public record AdminTemplateRequest(
        Long promptId,
        @NotNull String taskType,
        @NotBlank String title,
        String description,
        @NotBlank String templateContent,
        Double targetBand,
        boolean active
    ) {
    }

    private static String enumName(Enum<?> value) {
        return Objects.isNull(value) ? null : value.name();
    }
}
