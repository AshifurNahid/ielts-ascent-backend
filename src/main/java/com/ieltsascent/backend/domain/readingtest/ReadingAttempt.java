package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingAttemptMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reading_attempt")
@Getter
@Setter
public class ReadingAttempt extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    private Long readingTestId;

    private Long passageId;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant completedAt;

    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer correctAnswers;

    @Column(nullable = false)
    private Double scorePercent;

    @Column(nullable = false)
    private Integer totalTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingAttemptMode mode;
}
