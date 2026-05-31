package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingDifficulty;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingQuestionKind;
import com.ieltsascent.backend.infrastructure.persistence.StringListJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ReadingQuestion")
@Getter
@Setter
public class ReadingQuestion extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passage_id", nullable = false)
    private ReadingPassage passage;

    private Integer groupNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingQuestionKind type;

    @Column(nullable = false, length = 4000)
    private String prompt;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> options;

    @Column(nullable = false, length = 2000)
    private String correctAnswer;

    @Column(length = 4000)
    private String explanation;

    @Column(length = 1000)
    private String answerSourceHint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingDifficulty difficulty;

    @Column(nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private Boolean premium = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingContentStatus status = ReadingContentStatus.DRAFT;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> tags;

    @Column
    private Short wordLimit;

    @Column(length = 20)
    private String answerFormat;
}
