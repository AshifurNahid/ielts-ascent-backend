package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpeakingRecordingMetadata extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private SpeakingPrompt prompt;

    private String audioUrl;

    private Integer durationSeconds;

    @ManyToOne
    private SpeakingEvaluationResult evaluationResult;
}
