package com.ieltsascent.backend.domain.analytics;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class ProgressSnapshot extends BaseEntity {
    @ManyToOne
    private User user;

    private Double overallBand;

    private LocalDate snapshotDate;

}
