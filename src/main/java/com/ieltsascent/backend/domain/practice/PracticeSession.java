package com.ieltsascent.backend.domain.practice;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
public class PracticeSession extends BaseEntity {
    @ManyToOne
    private User user;

    private String skillType;

    private Long taskId;

    private Instant startedAt;

    private Instant completedAt;

}
