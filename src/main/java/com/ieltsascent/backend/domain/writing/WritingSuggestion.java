package com.ieltsascent.backend.domain.writing;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WritingSuggestion extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private WritingSubmission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WritingSuggestionType suggestionType;

    @Column(length = 1500)
    private String originalText;

    @Column(length = 2000)
    private String suggestedText;

    @Column(length = 1500)
    private String explanation;
}
