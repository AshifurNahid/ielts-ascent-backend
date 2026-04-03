package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarLessonExample;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarLessonExampleRepository extends JpaRepository<GrammarLessonExample, UUID> {
    List<GrammarLessonExample> findByLessonIdOrderByCreatedAtAsc(UUID lessonId);
}
