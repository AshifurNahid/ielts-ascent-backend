package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SpeakingEvaluationResult extends BaseEntity {
    private Double fluency;
    private Double pronunciation;
    private Double lexicalResource;
    private Double grammar;
    private Double overallBand;
    private String feedbackSummary;
}
