package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reading_question_attempt")
@Getter
@Setter
@NoArgsConstructor
public class ReadingQuestionAttempt extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_attempt_id", nullable = false)
    private ReadingAttempt readingAttempt;

    @Column(nullable = false)
    private UUID readingQuestionId;

    @Column(nullable = false)
    private UUID userId;

    @Column(length = 2000)
    private String userAnswer;

    @Column(nullable = false)
    private Boolean correct;

    @Column(nullable = false)
    private Integer timeSpentSeconds;
}
