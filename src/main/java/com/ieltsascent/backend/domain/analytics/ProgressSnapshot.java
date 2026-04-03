package com.ieltsascent.backend.domain.analytics;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class ProgressSnapshot extends BaseEntity {
    @ManyToOne
    private User user;

    private Double overallBand;

    private LocalDate snapshotDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getOverallBand() {
        return overallBand;
    }

    public void setOverallBand(Double overallBand) {
        this.overallBand = overallBand;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }
}
