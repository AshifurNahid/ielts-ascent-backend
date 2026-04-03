package com.ieltsascent.backend.domain.mocktest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class MockTestSectionResult extends BaseEntity {
    @ManyToOne
    private MockTestSession session;

    private String section;

    private Double bandScore;

    private String feedback;

    public MockTestSession getSession() {
        return session;
    }

    public void setSession(MockTestSession session) {
        this.session = session;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Double getBandScore() {
        return bandScore;
    }

    public void setBandScore(Double bandScore) {
        this.bandScore = bandScore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
