package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WritingSubmission extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private WritingPrompt prompt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingTaskType taskType;

    @Column(nullable = false, length = 15000)
    private String essayText;

    @Column(nullable = false)
    private Integer wordCount;

    @Column(nullable = false)
    private boolean timedMode;

    private Instant startedAt;

    private Instant submittedAt;

    private Long durationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingSubmissionStatus status;

    private Double overallBand;
    private Double taskResponseBand;
    private Double coherenceBand;
    private Double lexicalBand;
    private Double grammarBand;
    private Double evaluationConfidence;

    @Column(length = 2000)
    private String aiSummary;
}
