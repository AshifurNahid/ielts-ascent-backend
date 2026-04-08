package com.ieltsascent.backend.domain.grammar;

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
@Table(name = "grammar_lesson_example")
@Getter
@Setter
public class GrammarLessonExample extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private GrammarLesson lesson;

    @Column(nullable = false, length = 1500)
    private String correctSentence;

    @Column(length = 1500)
    private String incorrectSentence;

    @Column(nullable = false, length = 4000)
    private String explanation;

    @Column(nullable = false)
    private Boolean aiGenerated = false;

    @Column(nullable = false)
    private Boolean approved = false;
}
