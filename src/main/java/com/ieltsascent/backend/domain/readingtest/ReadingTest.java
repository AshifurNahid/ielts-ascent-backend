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
@Table(name = "reading_test")
@Getter
@Setter
public class ReadingTest extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    @Column(nullable = false)
    private Integer totalTimeMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingDifficulty difficulty;

    @Column(nullable = false)
    private Boolean premium = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingContentStatus status = ReadingContentStatus.DRAFT;
}
