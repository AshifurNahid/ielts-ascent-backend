package com.ieltsascent.backend.domain.readingtest;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_reading_profile")
@Getter
@Setter
public class UserReadingProfile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnglishLevel englishLevel;

    @Column(nullable = false)
    private Double currentIeltsBand;

    @Column(nullable = false)
    private Double targetIeltsBand;

    @Column(nullable = false)
    private Boolean premiumUser = false;
}
