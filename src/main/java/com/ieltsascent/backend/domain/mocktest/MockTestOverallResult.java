package com.ieltsascent.backend.domain.mocktest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;

@Entity
public class MockTestOverallResult extends BaseEntity {
    @OneToOne
    private MockTestSession session;

    private Double overallBand;

    private String summary;

    public MockTestSession getSession() {
        return session;
    }

    public void setSession(MockTestSession session) {
        this.session = session;
    }

    public Double getOverallBand() {
        return overallBand;
    }

    public void setOverallBand(Double overallBand) {
        this.overallBand = overallBand;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
