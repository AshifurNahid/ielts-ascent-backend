package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingAttempt;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingAttemptRepository extends JpaRepository<ReadingAttempt, UUID> {
    List<ReadingAttempt> findTop20ByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<ReadingAttempt> findByIdAndUserId(UUID id, UUID userId);
    List<ReadingAttempt> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(UUID userId, Instant after);
}
