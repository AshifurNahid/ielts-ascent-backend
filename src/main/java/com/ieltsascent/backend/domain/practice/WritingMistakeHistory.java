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
public class WritingMistakeHistory extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private WritingEvaluationResult evaluationResult;

    private String category;

    private String description;

    private Instant occurredAt;
}
