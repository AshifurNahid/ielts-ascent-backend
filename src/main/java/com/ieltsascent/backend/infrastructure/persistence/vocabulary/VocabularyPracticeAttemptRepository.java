package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.VocabularyPracticeAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyPracticeAttemptRepository extends JpaRepository<VocabularyPracticeAttempt, Long> {
    List<VocabularyPracticeAttempt> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);
}
