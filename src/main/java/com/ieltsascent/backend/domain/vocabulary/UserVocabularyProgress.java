package com.ieltsascent.backend.domain.vocabulary;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_vocabulary_progress")
@Getter
@Setter
@NoArgsConstructor
public class UserVocabularyProgress extends BaseEntity {
    @Column(nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vocabulary_word_id", nullable = false)
    private VocabularyWord vocabularyWord;

    @Column(nullable = false)
    private Integer exposureCount = 0;

    @Column(nullable = false)
    private Integer correctCount = 0;

    @Column(nullable = false)
    private Integer wrongCount = 0;

    @Column(nullable = false)
    private Double masteryLevel = 0.0;

    private Instant lastPracticedAt;

    private Instant nextRecommendedAt;

    @Column(nullable = false)
    private Boolean markedDifficult = false;
}
