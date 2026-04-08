package com.ieltsascent.backend.domain.auth;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
public class UserProfile extends BaseEntity {
    @Column(nullable = false)
    private Double targetBand;

    private Double currentBand;

    private LocalDate examDate;

    private String timezone;

    @Enumerated(EnumType.STRING)
    private StudyPreference studyPreference = StudyPreference.BALANCED;

    @Column(nullable = false)
    private Boolean onboardingCompleted = false;

    private Instant onboardingCompletedAt;

    @OneToOne(mappedBy = "profile")
    private User user;

    public enum StudyPreference {
        BALANCED,
        INTENSIVE,
        SPEAKING_FOCUS,
        WRITING_FOCUS
    }
}
