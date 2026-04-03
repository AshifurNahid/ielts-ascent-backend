package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WritingUserStats extends BaseEntity {
    @ManyToOne
    private User user;

    private String taskType;

    private Integer submissionCount;

    private Double averageOverallBand;

    private Double lastOverallBand;

    private Instant lastEvaluatedAt;
}
