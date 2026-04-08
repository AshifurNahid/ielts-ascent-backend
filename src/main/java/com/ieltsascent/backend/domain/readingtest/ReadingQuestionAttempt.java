package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reading_question_attempt")
@Getter
@Setter
public class ReadingQuestionAttempt extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_attempt_id", nullable = false)
    private ReadingAttempt readingAttempt;

    @Column(nullable = false)
    private Long readingQuestionId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 2000)
    private String userAnswer;

    @Column(nullable = false)
    private Boolean correct;

    @Column(nullable = false)
    private Integer timeSpentSeconds;
}
