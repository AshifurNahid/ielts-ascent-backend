package com.ieltsascent.backend.application.story;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltsascent.backend.domain.story.StoryEvaluation;
import com.ieltsascent.backend.domain.story.StorySubmission;
import com.ieltsascent.backend.infrastructure.persistence.StoryEvaluationRepository;
import com.ieltsascent.backend.infrastructure.persistence.StorySubmissionRepository;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StoryBuilderServiceTest {

    private StorySubmissionRepository submissionRepository;
    private StoryEvaluationRepository evaluationRepository;
    private StoryEvaluationEngine evaluationEngine;
    private StoryBuilderService service;

    @BeforeEach
    void setUp() {
        submissionRepository = mock(StorySubmissionRepository.class);
        evaluationRepository = mock(StoryEvaluationRepository.class);
        evaluationEngine = mock(StoryEvaluationEngine.class);
        service = new StoryBuilderService(submissionRepository, evaluationRepository, evaluationEngine, new ObjectMapper());
    }

    @Test
    void submitStoresOrderedPartsAndWordCount() {
        UUID userId = UUID.randomUUID();

        when(submissionRepository.save(any(StorySubmission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StorySubmission submission = service.submit(userId, "My topic", Map.of(
            "event", "I visited Tokyo",
            "setup", "Last summer",
            "lesson", "I learned patience"
        ));

        assertThat(submission.getUserId()).isEqualTo(userId);
        assertThat(submission.getWordCount()).isEqualTo(7);
        assertThat(submission.getPartsJson()).contains("setup").contains("event").contains("feelings").contains("outcome").contains("lesson");
    }

    @Test
    void evaluateUsesEngineAndPersistsEvaluation() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();

        StorySubmission submission = new StorySubmission();
        submission.setUserId(userId);
        submission.setTopic("Topic");
        submission.setPartsJson("{\"setup\":\"One\",\"event\":\"Two\"}");
        setEntityId(submission, submissionId);

        when(submissionRepository.findByIdAndUserId(submissionId, userId)).thenReturn(Optional.of(submission));
        when(evaluationRepository.findBySubmissionId(submissionId)).thenReturn(Optional.empty());

        StoryEvaluationEngine.StoryEvaluationResult engineResult = new StoryEvaluationEngine.StoryEvaluationResult(
            6.5,
            6.0,
            6.5,
            6.0,
            6.0,
            List.of("S1", "S2"),
            List.of("I1", "I2")
        );
        when(evaluationEngine.evaluate(eq(userId), eq("Topic"), eq("One Two"))).thenReturn(engineResult);
        when(evaluationRepository.save(any(StoryEvaluation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StoryEvaluationEngine.StoryEvaluationResult result = service.evaluate(userId, submissionId);

        assertThat(result).isEqualTo(engineResult);
        verify(evaluationRepository).save(any(StoryEvaluation.class));
    }

    @Test
    void historyReturnsSubmissionWithOptionalBand() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID submissionId = UUID.randomUUID();

        StorySubmission submission = new StorySubmission();
        submission.setTopic("Topic");
        submission.setWordCount(99);
        setEntityId(submission, submissionId);
        setCreatedAt(submission, Instant.parse("2026-03-28T00:00:00Z"));

        StoryEvaluation evaluation = new StoryEvaluation();
        evaluation.setOverallBand(6.5);

        when(submissionRepository.findByUserIdOrderByCreatedAtDesc(userId)).thenReturn(List.of(submission));
        when(evaluationRepository.findBySubmissionId(submissionId)).thenReturn(Optional.of(evaluation));

        List<StoryBuilderService.StoryHistoryItem> history = service.history(userId);

        assertThat(history).hasSize(1);
        assertThat(history.getFirst().overallBand()).isEqualTo(6.5);
        assertThat(history.getFirst().wordCount()).isEqualTo(99);
    }

    private void setEntityId(Object entity, UUID id) throws Exception {
        Field field = entity.getClass().getSuperclass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }

    private void setCreatedAt(Object entity, Instant createdAt) throws Exception {
        Field field = entity.getClass().getSuperclass().getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(entity, createdAt);
    }
}
