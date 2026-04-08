package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_grammar_progress")
@Getter
@Setter
public class UserGrammarProgress extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private GrammarTopic topic;

    @Column(nullable = false)
    private Integer completedLessons = 0;

    @Column(nullable = false)
    private Integer totalLessons = 0;

    @Column(nullable = false)
    private Double masteryScore = 0.0;

    private LocalDateTime lastPracticedAt;

    @Column(nullable = false)
    private Boolean unlocked = true;

    @Column(nullable = false)
    private Boolean completed = false;
}
