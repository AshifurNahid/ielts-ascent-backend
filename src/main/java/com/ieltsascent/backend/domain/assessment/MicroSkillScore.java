package com.ieltsascent.backend.domain.assessment;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class MicroSkillScore extends BaseEntity {
    @ManyToOne
    private DiagnosticResult diagnosticResult;

    private String skillName;

    private Integer score;

    private Integer confidence;

    public DiagnosticResult getDiagnosticResult() {
        return diagnosticResult;
    }

    public void setDiagnosticResult(DiagnosticResult diagnosticResult) {
        this.diagnosticResult = diagnosticResult;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }
}
