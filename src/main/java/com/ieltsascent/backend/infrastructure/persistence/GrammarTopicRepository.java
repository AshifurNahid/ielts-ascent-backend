package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLevel;
import com.ieltsascent.backend.domain.grammar.GrammarTopic;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, UUID> {
    @Query("""
        SELECT t FROM GrammarTopic t
        WHERE (:status IS NULL OR t.status = :status)
          AND (:level IS NULL OR t.level = :level)
          AND (:premium IS NULL OR t.premium = :premium)
          AND (:query IS NULL OR lower(t.title) LIKE concat('%', :query, '%') OR lower(coalesce(t.description, '')) LIKE concat('%', :query, '%'))
        ORDER BY t.orderIndex ASC, t.createdAt DESC
        """)
    Page<GrammarTopic> searchAdmin(
        @Param("status") GrammarContentStatus status,
        @Param("level") GrammarLevel level,
        @Param("premium") Boolean premium,
        @Param("query") String query,
        Pageable pageable
    );

    @Query("""
        SELECT t FROM GrammarTopic t
        WHERE t.status = :status
        ORDER BY t.orderIndex ASC
        """)
    List<GrammarTopic> findAllByStatusOrderByOrderIndexAsc(@Param("status") GrammarContentStatus status);
}
