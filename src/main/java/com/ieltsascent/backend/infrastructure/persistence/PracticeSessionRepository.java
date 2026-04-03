package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.PracticeSession;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PracticeSessionRepository extends JpaRepository<PracticeSession, UUID> {
    List<PracticeSession> findByUser_IdAndCompletedAtBetween(UUID userId, Instant from, Instant to);

    @Query("""
        SELECT s
        FROM PracticeSession s
        WHERE s.user.id = :userId
          AND (:skillType IS NULL OR s.skillType = :skillType)
          AND (:from IS NULL OR s.completedAt >= :from)
          AND (:to IS NULL OR s.completedAt <= :to)
        """)
    Page<PracticeSession> searchSessions(
        @Param("userId") UUID userId,
        @Param("skillType") String skillType,
        @Param("from") Instant from,
        @Param("to") Instant to,
        Pageable pageable
    );
}
