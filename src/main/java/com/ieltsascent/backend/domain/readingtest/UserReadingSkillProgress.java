package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingSkillStatus;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingSkillType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_reading_skill_progress")
@Getter
@Setter
public class UserReadingSkillProgress extends BaseEntity {
    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingSkillType skillType;

    @Column(nullable = false)
    private String skillKey;

    @Column(nullable = false)
    private Integer totalAttempts = 0;

    @Column(nullable = false)
    private Integer totalCorrect = 0;

    @Column(nullable = false)
    private Integer recentAttempts = 0;

    @Column(nullable = false)
    private Integer recentCorrect = 0;

    @Column(nullable = false)
    private Double averageTimeSeconds = 0d;

    @Column(nullable = false)
    private Double masteryScore = 0d;

    @Column(nullable = false)
    private Double weakScore = 100d;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingSkillStatus status = ReadingSkillStatus.WEAK;

    private Instant lastPracticedAt;

    private Instant lastImprovedAt;
}
