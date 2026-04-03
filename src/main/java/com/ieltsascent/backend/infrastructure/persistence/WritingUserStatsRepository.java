package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.WritingUserStats;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingUserStatsRepository extends JpaRepository<WritingUserStats, UUID> {
    Optional<WritingUserStats> findByUserIdAndTaskType(UUID userId, String taskType);
}
