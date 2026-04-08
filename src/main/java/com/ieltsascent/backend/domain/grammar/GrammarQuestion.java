package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grammar_question")
@Getter
@Setter
public class GrammarQuestion extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private GrammarTopic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private GrammarLesson lesson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GrammarQuestionType type;

    @Column(nullable = false, length = 4000)
    private String question;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "grammar_question_option", joinColumns = @JoinColumn(name = "grammar_question_id"))
    @Column(name = "option_text", nullable = false, length = 500)
    private Set<String> options = new HashSet<>();

    @Column(nullable = false, length = 1000)
    private String correctAnswer;

    @Column(length = 4000)
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GrammarDifficulty difficulty;

    @Column(nullable = false)
    private Boolean premium = false;

    @Column(nullable = false)
    private Double ieltsBandMin;

    @Column(nullable = false)
    private Double ieltsBandMax;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "grammar_question_tag", joinColumns = @JoinColumn(name = "grammar_question_id"))
    @Column(name = "tag", nullable = false, length = 100)
    private Set<String> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GrammarContentStatus status = GrammarContentStatus.DRAFT;

    @Column(nullable = false)
    private Boolean aiGenerated = false;

    @Column(nullable = false)
    private Boolean reviewedByAdmin = false;
}
