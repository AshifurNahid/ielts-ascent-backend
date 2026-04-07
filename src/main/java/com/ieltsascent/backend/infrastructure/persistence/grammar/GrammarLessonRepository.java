package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLesson;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarLessonRepository extends JpaRepository<GrammarLesson, Long> {
    List<GrammarLesson> findByTopicIdOrderByOrderIndexAsc(Long topicId);

    List<GrammarLesson> findByTopicIdAndStatusOrderByOrderIndexAsc(Long topicId, GrammarContentStatus status);

    long countByTopicIdAndStatus(Long topicId, GrammarContentStatus status);
}
