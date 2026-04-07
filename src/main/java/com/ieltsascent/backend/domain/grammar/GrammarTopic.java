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
@Table(name = "grammar_topic")
@Getter
@Setter
@NoArgsConstructor
public class GrammarTopic extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(length = 4000)
    private String description;

    private String icon;

    @Column(nullable = false)
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GrammarLevel level;

    @Column(nullable = false)
    private Double ieltsBandMin;

    @Column(nullable = false)
    private Double ieltsBandMax;

    @Column(nullable = false)
    private Boolean premium = false;

    @Column(nullable = false)
    private Boolean unlockedByDefault = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GrammarContentStatus status = GrammarContentStatus.DRAFT;

    private Long prerequisiteTopicId;
}
