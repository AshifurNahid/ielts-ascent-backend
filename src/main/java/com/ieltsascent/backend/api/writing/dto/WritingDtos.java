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
import java.util.List;
import java.util.UUID;

public final class WritingDtos {
    private WritingDtos() {
    }

    public record PromptResponse(
        UUID id,
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
        Instant createdAt,
        Instant updatedAt
    ) {
        public static PromptResponse from(WritingPrompt prompt) {
            return new PromptResponse(
                prompt.getId(), prompt.getTitle(), prompt.getTaskType().name(), prompt.getPromptText(), prompt.getInstructions(),
                prompt.getTags(), prompt.getDifficulty().name(), prompt.getIeltsBandMin(), prompt.getIeltsBandMax(),
                prompt.isPremium(), prompt.getStatus().name(), prompt.isTemplateEnabled(), prompt.getCreatedAt(), prompt.getUpdatedAt()
            );
        }
    }

    public record TemplateResponse(UUID id, UUID promptId, String taskType, String title, String description, String templateContent,
                                   Double targetBand, boolean active, Instant createdAt, Instant updatedAt) {
        public static TemplateResponse from(WritingTemplate template) {
            return new TemplateResponse(
                template.getId(), template.getPrompt() == null ? null : template.getPrompt().getId(), template.getTaskType().name(),
                template.getTitle(), template.getDescription(), template.getTemplateContent(), template.getTargetBand(), template.isActive(),
                template.getCreatedAt(), template.getUpdatedAt()
            );
        }
    }

    public record SubmissionResponse(
        UUID id,
        UUID promptId,
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
                submission.getTaskType().name(),
                submission.getEssayText(),
                submission.getWordCount(),
                submission.isTimedMode(),
                submission.getStartedAt(),
                submission.getSubmittedAt(),
                submission.getDurationSeconds(),
                submission.getStatus().name(),
                submission.getOverallBand(),
                submission.getTaskResponseBand(),
                submission.getCoherenceBand(),
                submission.getLexicalBand(),
                submission.getGrammarBand(),
                submission.getEvaluationConfidence(),
                submission.getAiSummary(),
                weakPoints.stream().map(WeakPointResponse::from).toList(),
                suggestions.stream().map(SuggestionResponse::from).toList()
            );
        }
    }

    public record WeakPointResponse(UUID id, String category, String weakKey, String severity, String explanation, String suggestion) {
        public static WeakPointResponse from(WritingWeakPoint weakPoint) {
            return new WeakPointResponse(
                weakPoint.getId(), weakPoint.getCategory().name(), weakPoint.getWeakKey(), weakPoint.getSeverity().name(),
                weakPoint.getExplanation(), weakPoint.getSuggestion()
            );
        }
    }

    public record SuggestionResponse(UUID id, String suggestionType, String originalText, String suggestedText, String explanation) {
        public static SuggestionResponse from(WritingSuggestion suggestion) {
            return new SuggestionResponse(
                suggestion.getId(), suggestion.getSuggestionType().name(), suggestion.getOriginalText(), suggestion.getSuggestedText(), suggestion.getExplanation()
            );
        }
    }

    public record ProgressResponse(Long totalSubmissions, Double averageBand, Double latestBand, Double bestBand, Double trendValue,
                                   Double last30DayImprovement) {
        public static ProgressResponse from(UserWritingProgress progress) {
            return new ProgressResponse(
                progress == null ? 0L : progress.getTotalSubmissions(),
                progress == null ? null : progress.getAverageBand(),
                progress == null ? null : progress.getLatestBand(),
                progress == null ? null : progress.getBestBand(),
                progress == null ? null : progress.getTrendValue(),
                progress == null ? null : progress.getLast30DayImprovement()
            );
        }
    }

    public record StartSubmissionRequest(@NotNull UUID promptId, boolean timedMode) {
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
        UUID promptId,
        @NotNull String taskType,
        @NotBlank String title,
        String description,
        @NotBlank String templateContent,
        Double targetBand,
        boolean active
    ) {
    }
}
