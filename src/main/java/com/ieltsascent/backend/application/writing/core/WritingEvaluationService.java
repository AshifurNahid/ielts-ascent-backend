package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.UserWritingProfile;
import com.ieltsascent.backend.domain.writing.WeakPointCategory;
import com.ieltsascent.backend.domain.writing.WeakPointSeverity;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import com.ieltsascent.backend.domain.writing.WritingSuggestionType;
import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import com.ieltsascent.backend.infrastructure.persistence.writing.UserWritingProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSuggestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingWeakPointRepository;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingEvaluationService {
    private final WritingAiService writingAiService;
    private final WritingWeakPointRepository writingWeakPointRepository;
    private final WritingSuggestionRepository writingSuggestionRepository;
    private final UserWritingProfileRepository userWritingProfileRepository;
    private final UserWritingProgressService userWritingProgressService;
    private final WritingWeakPointService writingWeakPointService;

    @Transactional
    public EvaluationOutput evaluateAndPersist(WritingSubmission submission) {
        int paragraphCount = (int) submission.getEssayText().lines().filter(line -> !line.isBlank()).count();
        int sentenceCount = submission.getEssayText().split("[.!?]+\\s*").length;

        String historySummary = "Recent weak categories: " + writingWeakPointService.recurringWeakPoints(submission.getUser().getId()).stream()
            .limit(4)
            .map(WritingWeakPointService.RecurringWeakPointSummary::category)
            .toList();

        WritingAiService.AiEvaluationResponse ai = writingAiService.evaluateEssay(
            submission.getTaskType(),
            submission.getPrompt() == null ? "" : submission.getPrompt().getPromptText(),
            submission.getEssayText(),
            historySummary + "; paragraphs=" + paragraphCount + "; sentences=" + sentenceCount
        );

        boolean successful = ai.overallBand() != null;
        if (!successful) {
            submission.setStatus(WritingSubmissionStatus.EVALUATION_FAILED);
            submission.setAiSummary("Evaluation pending. Please retry shortly.");
            return new EvaluationOutput(false, paragraphCount, sentenceCount);
        }

        submission.setOverallBand(clampBand(ai.overallBand()));
        submission.setTaskResponseBand(clampBand(ai.taskResponseBand()));
        submission.setCoherenceBand(clampBand(ai.coherenceBand()));
        submission.setLexicalBand(clampBand(ai.lexicalBand()));
        submission.setGrammarBand(clampBand(ai.grammarBand()));
        submission.setEvaluationConfidence(ai.confidence() == null ? 0.7 : Math.max(0.0, Math.min(1.0, ai.confidence())));
        submission.setAiSummary(ai.summary());
        submission.setStatus(WritingSubmissionStatus.EVALUATED);

        writingWeakPointRepository.saveAll(ai.weakPoints().stream().limit(8).map(w -> {
            WritingWeakPoint weakPoint = new WritingWeakPoint();
            weakPoint.setSubmission(submission);
            weakPoint.setUser(submission.getUser());
            weakPoint.setCategory(parseCategory(w.category()));
            weakPoint.setWeakKey(w.weakKey() == null ? "general" : w.weakKey().toLowerCase(Locale.ROOT));
            weakPoint.setSeverity(parseSeverity(w.severity()));
            weakPoint.setExplanation(w.explanation());
            weakPoint.setSuggestion(w.suggestion());
            return weakPoint;
        }).toList());

        writingSuggestionRepository.saveAll(ai.suggestions().stream().limit(6).map(s -> {
            WritingSuggestion suggestion = new WritingSuggestion();
            suggestion.setSubmission(submission);
            suggestion.setUser(submission.getUser());
            suggestion.setSuggestionType(parseSuggestionType(s.suggestionType()));
            suggestion.setOriginalText(s.originalText());
            suggestion.setSuggestedText(s.suggestedText());
            suggestion.setExplanation(s.explanation());
            return suggestion;
        }).toList());

        updateProfile(submission.getUser(), submission.getOverallBand());
        userWritingProgressService.recalculate(submission.getUser());
        return new EvaluationOutput(true, paragraphCount, sentenceCount);
    }

    private void updateProfile(User user, Double currentBand) {
        UserWritingProfile profile = userWritingProfileRepository.findByUserId(user.getId()).orElseGet(() -> {
            UserWritingProfile p = new UserWritingProfile();
            p.setUser(user);
            return p;
        });
        profile.setCurrentEstimatedWritingBand(currentBand);
        profile.setLastSubmissionAt(Instant.now());
        profile.setWeakCategories(writingWeakPointService.recurringWeakPoints(user.getId()).stream().limit(5).map(WritingWeakPointService.RecurringWeakPointSummary::category).toList());
        userWritingProfileRepository.save(profile);
    }

    private WeakPointCategory parseCategory(String value) {
        try {
            return WeakPointCategory.valueOf(value == null ? "GRAMMAR" : value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return WeakPointCategory.GRAMMAR;
        }
    }

    private WeakPointSeverity parseSeverity(String value) {
        try {
            return WeakPointSeverity.valueOf(value == null ? "MEDIUM" : value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return WeakPointSeverity.MEDIUM;
        }
    }

    private WritingSuggestionType parseSuggestionType(String value) {
        try {
            return WritingSuggestionType.valueOf(value == null ? "STRUCTURE" : value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return WritingSuggestionType.STRUCTURE;
        }
    }

    private double clampBand(Double value) {
        if (value == null) {
            return 0;
        }
        return Math.max(0.0, Math.min(9.0, Math.round(value * 2.0) / 2.0));
    }

    public record EvaluationOutput(boolean evaluated, int paragraphCount, int sentenceCount) {
    }
}
