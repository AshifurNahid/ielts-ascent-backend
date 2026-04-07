package com.ieltsascent.backend.domain.assessment;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class DiagnosticResult extends BaseEntity {
    @ManyToOne
    private DiagnosticSession session;

    private Double overallBand;

    private UUID summaryId;

}
