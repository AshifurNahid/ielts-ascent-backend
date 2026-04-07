package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.studyplan.TaskCompletion;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Long> {
    List<TaskCompletion> findByUserIdAndCompletedAtAfter(Long userId, Instant completedAfter);
}
