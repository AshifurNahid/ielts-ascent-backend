package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarPracticeAttempt;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarPracticeAttemptRepository extends JpaRepository<GrammarPracticeAttempt, UUID> {
    List<GrammarPracticeAttempt> findTop200ByUserIdOrderByCreatedAtDesc(UUID userId);

    List<GrammarPracticeAttempt> findByUserIdAndCreatedAtAfter(UUID userId, Instant after);
}
