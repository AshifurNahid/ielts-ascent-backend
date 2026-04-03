package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarQuestion;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrammarQuestionRepository extends JpaRepository<GrammarQuestion, UUID> {
    @Query("""
        SELECT q FROM GrammarQuestion q
        WHERE (:status IS NULL OR q.status = :status)
          AND (:type IS NULL OR q.type = :type)
          AND (:topicId IS NULL OR q.topic.id = :topicId)
          AND (:premium IS NULL OR q.premium = :premium)
          AND (:query IS NULL OR lower(q.question) LIKE concat('%', :query, '%'))
        ORDER BY q.createdAt DESC
        """)
    Page<GrammarQuestion> searchAdmin(
        @Param("status") GrammarContentStatus status,
        @Param("type") GrammarQuestionType type,
        @Param("topicId") UUID topicId,
        @Param("premium") Boolean premium,
        @Param("query") String query,
        Pageable pageable
    );

    List<GrammarQuestion> findByTopicIdAndStatus(UUID topicId, GrammarContentStatus status);

    List<GrammarQuestion> findByTypeAndStatus(GrammarQuestionType type, GrammarContentStatus status);

    long countByTopicIdAndStatus(UUID topicId, GrammarContentStatus status);
}
