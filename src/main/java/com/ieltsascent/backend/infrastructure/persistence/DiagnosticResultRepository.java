package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticResultRepository extends JpaRepository<DiagnosticResult, Long> {
    Optional<DiagnosticResult> findBySessionId(Long sessionId);

    Optional<DiagnosticResult> findFirstBySessionUserIdOrderByCreatedAtDesc(Long userId);
    Optional<DiagnosticResult> findFirstBySessionUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long userId, Instant before);

    List<DiagnosticResult> findTop7BySessionUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsBySessionUserId(Long userId);
}
