package com.ieltsascent.backend.domain.grammar;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "grammar_drill_template")
@Getter
@Setter
@NoArgsConstructor
public class GrammarDrillTemplate extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String icon;

    @Column(nullable = false)
    private Integer estimatedTimeMinutes;

    @Column(nullable = false)
    private Integer questionCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GrammarQuestionType type;

    @Column(nullable = false)
    private Boolean active = true;
}
