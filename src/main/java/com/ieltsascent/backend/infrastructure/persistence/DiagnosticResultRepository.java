package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticResultRepository extends JpaRepository<DiagnosticResult, UUID> {
    Optional<DiagnosticResult> findBySessionId(UUID sessionId);

    Optional<DiagnosticResult> findFirstBySessionUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<DiagnosticResult> findFirstBySessionUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(UUID userId, Instant before);

    List<DiagnosticResult> findTop7BySessionUserIdOrderByCreatedAtDesc(UUID userId);

    boolean existsBySessionUserId(UUID userId);
}
