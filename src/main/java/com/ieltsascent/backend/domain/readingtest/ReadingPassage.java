package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingDifficulty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reading_passage")
@Getter
@Setter
public class ReadingPassage extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 16000)
    private String content;

    @Column(length = 1000)
    private String shortDescription;

    @Column(nullable = false)
    private String topicTag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingDifficulty difficulty;

    @Column(nullable = false)
    private Double ieltsBandMin;

    @Column(nullable = false)
    private Double ieltsBandMax;

    @Column(nullable = false)
    private Integer estimatedReadingMinutes;

    @Column(nullable = false)
    private Boolean premium = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingContentStatus status = ReadingContentStatus.DRAFT;
}
