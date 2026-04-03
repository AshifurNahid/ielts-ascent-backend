package com.ieltsascent.backend.domain.studyplan;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.Instant;

@Entity
public class TaskCompletion extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private StudyTask studyTask;

    private Instant completedAt;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudyTask getStudyTask() {
        return studyTask;
    }

    public void setStudyTask(StudyTask studyTask) {
        this.studyTask = studyTask;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
