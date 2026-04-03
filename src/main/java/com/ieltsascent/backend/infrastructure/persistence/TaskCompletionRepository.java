package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.studyplan.TaskCompletion;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, UUID> {
    List<TaskCompletion> findByUserIdAndCompletedAtAfter(UUID userId, Instant completedAfter);
}
