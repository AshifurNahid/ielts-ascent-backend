package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_grammar_profile")
@Getter
@Setter
@NoArgsConstructor
public class UserGrammarProfile extends BaseEntity {
    @Column(nullable = false, unique = true)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnglishLevel englishLevel = EnglishLevel.BEGINNER;

    private Double currentIeltsBand;

    @Column(nullable = false)
    private Double targetIeltsBand;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_grammar_weak_topic", joinColumns = @JoinColumn(name = "profile_id"))
    @Column(name = "topic_title", nullable = false, length = 120)
    private Set<String> weakTopics = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_grammar_weak_drill_type", joinColumns = @JoinColumn(name = "profile_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "drill_type", nullable = false, length = 30)
    private Set<GrammarQuestionType> weakDrillTypes = new HashSet<>();

    @Column(nullable = false)
    private Boolean premiumUser = false;
}
