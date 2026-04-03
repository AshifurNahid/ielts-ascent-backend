package com.ieltsascent.backend.api.story;

import com.ieltsascent.backend.application.story.StoryBuilderService;
import com.ieltsascent.backend.application.story.StoryEvaluationEngine;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoryBuilderMapperImplTest {

    private final StoryBuilderMapper mapper = new StoryBuilderMapperImpl();

    @Test
    void mapsEvaluationResult() {
        StoryEvaluationEngine.StoryEvaluationResult result = new StoryEvaluationEngine.StoryEvaluationResult(
            6.5,
            6.0,
            7.0,
            6.5,
            6.5,
            List.of("Clear sequence", "Relevant details"),
            List.of("Use richer vocabulary", "Improve tense consistency")
        );

        var response = mapper.toEvaluationResponse(result);

        assertThat(response.fluency()).isEqualTo(6.5);
        assertThat(response.coherence()).isEqualTo(6.0);
        assertThat(response.vocabulary()).isEqualTo(7.0);
        assertThat(response.grammar()).isEqualTo(6.5);
        assertThat(response.strengths()).containsExactly("Clear sequence", "Relevant details");
        assertThat(response.improvements()).containsExactly("Use richer vocabulary", "Improve tense consistency");
    }

    @Test
    void mapsHistoryItem() {
        UUID submissionId = UUID.randomUUID();
        Instant submittedAt = Instant.now();
        StoryBuilderService.StoryHistoryItem item = new StoryBuilderService.StoryHistoryItem(
            submissionId,
            "A memorable journey",
            142,
            submittedAt,
            6.5
        );

        var response = mapper.toHistoryResponse(item);

        assertThat(response.submissionId()).isEqualTo(submissionId);
        assertThat(response.topic()).isEqualTo("A memorable journey");
        assertThat(response.wordCount()).isEqualTo(142);
        assertThat(response.submittedAt()).isEqualTo(submittedAt);
        assertThat(response.overallBand()).isEqualTo(6.5);
    }
}
