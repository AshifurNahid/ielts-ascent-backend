package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class SpeakingRecordingMetadata extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private SpeakingPrompt prompt;

    private String audioUrl;

    private Integer durationSeconds;

    @ManyToOne
    private SpeakingEvaluationResult evaluationResult;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SpeakingPrompt getPrompt() {
        return prompt;
    }

    public void setPrompt(SpeakingPrompt prompt) {
        this.prompt = prompt;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public SpeakingEvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(SpeakingEvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }
}
