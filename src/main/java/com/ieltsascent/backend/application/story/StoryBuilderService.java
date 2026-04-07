package com.ieltsascent.backend.application.story;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.story.StoryEvaluation;
import com.ieltsascent.backend.domain.story.StorySubmission;
import com.ieltsascent.backend.infrastructure.persistence.StoryEvaluationRepository;
import com.ieltsascent.backend.infrastructure.persistence.StorySubmissionRepository;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoryBuilderService {
    private static final List<String> ORDER = List.of("setup", "event", "feelings", "outcome", "lesson");

    private final StorySubmissionRepository submissionRepository;
    private final StoryEvaluationRepository evaluationRepository;
    private final StoryEvaluationEngine evaluationEngine;
    private final ObjectMapper objectMapper;

    @Transactional
    public StorySubmission submit(Long userId, String topic, Map<String, String> parts) {
        Map<String, String> sanitized = sanitize(parts);
        StorySubmission submission = new StorySubmission();
        submission.setUserId(userId);
        submission.setTopic(topic == null ? "" : topic.trim());
        submission.setPartsJson(toJson(sanitized));
        submission.setWordCount(countWords(joinStory(sanitized)));
        return submissionRepository.save(submission);
    }

    @Transactional
    public StoryEvaluationEngine.StoryEvaluationResult evaluate(Long userId, Long submissionId) {
        StorySubmission submission = submissionRepository.findByIdAndUserId(submissionId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Story submission not found"));
        Map<String, String> parts = parseParts(submission.getPartsJson());
        String storyText = joinStory(parts);
        StoryEvaluationEngine.StoryEvaluationResult result = evaluationEngine.evaluate(userId, submission.getTopic(), storyText);

        StoryEvaluation evaluation = evaluationRepository.findBySubmissionId(submission.getId())
            .orElseGet(StoryEvaluation::new);
        evaluation.setSubmission(submission);
        evaluation.setFluencyScore(result.fluency());
        evaluation.setCoherenceScore(result.coherence());
        evaluation.setVocabularyScore(result.vocabulary());
        evaluation.setGrammarScore(result.grammar());
        evaluation.setOverallBand(result.overallBand());
        evaluation.setStrengthsJson(toJson(result.strengths()));
        evaluation.setImprovementsJson(toJson(result.improvements()));
        evaluation.setEvaluatedAt(Instant.now());
        evaluationRepository.save(evaluation);

        return result;
    }

    @Transactional(readOnly = true)
    public List<StoryHistoryItem> history(Long userId) {
        List<StorySubmission> submissions = submissionRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return submissions.stream().map(submission -> {
            StoryEvaluation evaluation = evaluationRepository.findBySubmissionId(submission.getId()).orElse(null);
            return new StoryHistoryItem(
                submission.getId(),
                submission.getTopic(),
                submission.getWordCount(),
                submission.getCreatedAt(),
                evaluation == null ? null : evaluation.getOverallBand()
            );
        }).toList();
    }

    private Map<String, String> sanitize(Map<String, String> parts) {
        Map<String, String> sanitized = new LinkedHashMap<>();
        Map<String, String> source = parts == null ? Map.of() : parts;
        for (String key : ORDER) {
            sanitized.put(key, source.getOrDefault(key, "").trim());
        }
        return sanitized;
    }

    private String joinStory(Map<String, String> parts) {
        return ORDER.stream().map(k -> parts.getOrDefault(k, "").trim()).filter(v -> !v.isBlank()).reduce((a, b) -> a + " " + b)
            .orElse("");
    }

    private int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize story payload");
        }
    }

    private Map<String, String> parseParts(String raw) {
        try {
            return objectMapper.readValue(raw, new TypeReference<>() {
            });
        } catch (Exception ex) {
            return Map.of();
        }
    }

    public record StoryHistoryItem(Long submissionId, String topic, Integer wordCount, Instant submittedAt, Double overallBand) {
    }
}
