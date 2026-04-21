package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserWritingProgress extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Column(nullable = false)
    private Long totalSubmissions;

    private Double averageBand;
    private Double latestBand;
    private Double bestBand;
    private Double trendValue;
    private Double last30DayImprovement;
    private Double last30DayNetImprovement;
    private Double last30DayVolatility;
}
