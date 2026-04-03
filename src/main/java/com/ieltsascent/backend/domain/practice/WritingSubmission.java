package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.content.WritingPrompt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.Instant;

@Entity
public class WritingSubmission extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private WritingPrompt prompt;

    private String taskType;

    @Column(length = 10000)
    private String essayText;

    private Integer wordCount;

    private Instant submittedAt;

    @Enumerated(EnumType.STRING)
    private WritingSubmissionStatus status;

    @OneToOne(mappedBy = "submission")
    private WritingEvaluationResult evaluationResult;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WritingPrompt getPrompt() {
        return prompt;
    }

    public void setPrompt(WritingPrompt prompt) {
        this.prompt = prompt;
    }

    public String getEssayText() {
        return essayText;
    }

    public void setEssayText(String essayText) {
        this.essayText = essayText;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public WritingSubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(WritingSubmissionStatus status) {
        this.status = status;
    }

    public WritingEvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void setEvaluationResult(WritingEvaluationResult evaluationResult) {
        this.evaluationResult = evaluationResult;
    }
}
