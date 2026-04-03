package com.ieltsascent.backend.domain.assessment;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class DiagnosticResult extends BaseEntity {
    @ManyToOne
    private DiagnosticSession session;

    private Double overallBand;

    private UUID summaryId;

    public DiagnosticSession getSession() {
        return session;
    }

    public void setSession(DiagnosticSession session) {
        this.session = session;
    }

    public Double getOverallBand() {
        return overallBand;
    }

    public void setOverallBand(Double overallBand) {
        this.overallBand = overallBand;
    }

    public UUID getSummaryId() {
        return summaryId;
    }

    public void setSummaryId(UUID summaryId) {
        this.summaryId = summaryId;
    }
}
