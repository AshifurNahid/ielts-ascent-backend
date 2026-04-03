package com.ieltsascent.backend.domain.vocabulary;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vocabulary_example")
@Getter
@Setter
@NoArgsConstructor
public class VocabularyExample extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vocabulary_word_id", nullable = false)
    private VocabularyWord vocabularyWord;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VocabularyExampleType type;

    @Column(nullable = false, length = 2000)
    private String sentence;

    @Column(nullable = false)
    private Boolean aiGenerated = false;

    @Column(nullable = false)
    private Boolean approved = false;
}
