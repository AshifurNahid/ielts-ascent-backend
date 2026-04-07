package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingAttempt;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingAttemptRepository extends JpaRepository<ReadingAttempt, Long> {
    List<ReadingAttempt> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<ReadingAttempt> findByIdAndUserId(Long id, Long userId);
    List<ReadingAttempt> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, Instant after);
}
