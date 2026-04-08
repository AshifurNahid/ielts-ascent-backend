package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grammar_practice_attempt")
@Getter
@Setter
public class GrammarPracticeAttempt extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long grammarQuestionId;

    @Column(nullable = false)
    private Long topicId;

    private Long lessonId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GrammarQuestionType questionType;

    @Column(length = 2000)
    private String userAnswer;

    @Column(nullable = false)
    private Boolean correct;
}
