package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLesson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarLessonRepository extends JpaRepository<GrammarLesson, UUID> {
    List<GrammarLesson> findByTopicIdOrderByOrderIndexAsc(UUID topicId);

    List<GrammarLesson> findByTopicIdAndStatusOrderByOrderIndexAsc(UUID topicId, GrammarContentStatus status);

    long countByTopicIdAndStatus(UUID topicId, GrammarContentStatus status);
}
