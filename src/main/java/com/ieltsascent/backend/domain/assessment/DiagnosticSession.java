package com.ieltsascent.backend.domain.assessment;

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
public class DiagnosticSession extends BaseEntity {
    @ManyToOne
    private User user;

    private Instant startedAt;

    private Instant completedAt;

}
