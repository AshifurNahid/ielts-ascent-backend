package com.ieltsascent.backend.domain.assessment;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class MicroSkillScore extends BaseEntity {
    @ManyToOne
    private DiagnosticResult diagnosticResult;

    private String skillName;

    private Integer score;

    private Integer confidence;

}
