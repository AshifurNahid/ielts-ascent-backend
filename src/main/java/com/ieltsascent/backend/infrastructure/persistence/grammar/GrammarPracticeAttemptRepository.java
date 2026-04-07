package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarPracticeAttempt;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarPracticeAttemptRepository extends JpaRepository<GrammarPracticeAttempt, Long> {
    List<GrammarPracticeAttempt> findTop200ByUserIdOrderByCreatedAtDesc(Long userId);

    List<GrammarPracticeAttempt> findByUserIdAndCreatedAtAfter(Long userId, Instant after);
}
