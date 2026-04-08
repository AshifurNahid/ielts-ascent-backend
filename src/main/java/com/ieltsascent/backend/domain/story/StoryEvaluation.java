package com.ieltsascent.backend.domain.story;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StoryEvaluation extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false, unique = true)
    private StorySubmission submission;

    @Column(nullable = false)
    private Double fluencyScore;

    @Column(nullable = false)
    private Double coherenceScore;

    @Column(nullable = false)
    private Double vocabularyScore;

    @Column(nullable = false)
    private Double grammarScore;

    @Column(nullable = false)
    private Double overallBand;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String strengthsJson;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String improvementsJson;

    @Column(nullable = false)
    private LocalDateTime evaluatedAt;
}
