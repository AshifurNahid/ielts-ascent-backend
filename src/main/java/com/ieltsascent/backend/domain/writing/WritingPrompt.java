package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.infrastructure.persistence.StringListJsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WritingPrompt extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingTaskType taskType;

    @Column(nullable = false, length = 4000)
    private String promptText;

    @Column(length = 2000)
    private String instructions;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingDifficulty difficulty;

    @Column(nullable = false)
    private Double ieltsBandMin;

    @Column(nullable = false)
    private Double ieltsBandMax;

    @Column(nullable = false)
    private boolean premium;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingPromptStatus status;

    @Column(nullable = false)
    private boolean templateEnabled;
}
