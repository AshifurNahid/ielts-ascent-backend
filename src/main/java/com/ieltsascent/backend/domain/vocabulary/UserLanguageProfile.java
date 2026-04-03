package com.ieltsascent.backend.domain.vocabulary;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_language_profile")
@Getter
@Setter
@NoArgsConstructor
public class UserLanguageProfile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnglishLevel englishLevel = EnglishLevel.BEGINNER;

    private Double currentIeltsBand;

    private Double targetIeltsBand;

    @ElementCollection
    @CollectionTable(name = "user_language_profile_weak_tag", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "tag", nullable = false, length = 100)
    private Set<String> weakTags = new HashSet<>();

    @Column(nullable = false)
    private Boolean premiumUser = false;
}
