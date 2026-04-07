package com.ieltsascent.backend.domain.analytics;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.Instant;

@Getter
@Entity
public class MistakeHistory extends BaseEntity {
    @ManyToOne
    private User user;

    private String skillArea;

    private String description;

    private Instant occurredAt;

    public void setUser(User user) {
        this.user = user;
    }

    public void setSkillArea(String skillArea) {
        this.skillArea = skillArea;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}
