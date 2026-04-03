package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grammar_practice_attempt")
@Getter
@Setter
@NoArgsConstructor
public class GrammarPracticeAttempt extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID grammarQuestionId;

    @Column(nullable = false)
    private UUID topicId;

    private UUID lessonId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GrammarQuestionType questionType;

    @Column(length = 2000)
    private String userAnswer;

    @Column(nullable = false)
    private Boolean correct;
}
