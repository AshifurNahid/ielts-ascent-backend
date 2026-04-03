package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;

@Entity
public class SpeakingEvaluationResult extends BaseEntity {
    private Double fluency;
    private Double pronunciation;
    private Double lexicalResource;
    private Double grammar;
    private Double overallBand;
    private String feedbackSummary;

    public Double getFluency() {
        return fluency;
    }

    public void setFluency(Double fluency) {
        this.fluency = fluency;
    }

    public Double getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(Double pronunciation) {
        this.pronunciation = pronunciation;
    }

    public Double getLexicalResource() {
        return lexicalResource;
    }

    public void setLexicalResource(Double lexicalResource) {
        this.lexicalResource = lexicalResource;
    }

    public Double getGrammar() {
        return grammar;
    }

    public void setGrammar(Double grammar) {
        this.grammar = grammar;
    }

    public Double getOverallBand() {
        return overallBand;
    }

    public void setOverallBand(Double overallBand) {
        this.overallBand = overallBand;
    }

    public String getFeedbackSummary() {
        return feedbackSummary;
    }

    public void setFeedbackSummary(String feedbackSummary) {
        this.feedbackSummary = feedbackSummary;
    }
}
