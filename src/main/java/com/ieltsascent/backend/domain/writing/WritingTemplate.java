package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WritingTemplate extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private WritingPrompt prompt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingTaskType taskType;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, length = 8000)
    private String templateContent;

    private Double targetBand;

    @Column(nullable = false)
    private boolean active;
}
