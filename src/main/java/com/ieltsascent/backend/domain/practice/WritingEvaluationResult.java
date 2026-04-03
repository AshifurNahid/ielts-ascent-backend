package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.infrastructure.persistence.JsonNodeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tools.jackson.databind.JsonNode;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WritingEvaluationResult extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "submission_id", unique = true)
    private WritingSubmission submission;

    @Column(name = "task_response")
    private Double taskResponseBand;

    @Column(name = "coherence")
    private Double coherenceBand;

    @Column(name = "lexical_resource")
    private Double lexicalBand;

    @Column(name = "grammar")
    private Double grammarBand;

    private Double overallBand;
    private String feedbackSummary;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode jsonFeedback;

    private String provider;
    private String model;
    private String promptVersion;
    private Instant evaluatedAt;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode mistakes;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode rewriteExercises;

    @Convert(converter = JsonNodeConverter.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode improvementTips;
}
