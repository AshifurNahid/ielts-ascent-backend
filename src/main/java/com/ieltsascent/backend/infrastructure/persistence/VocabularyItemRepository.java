package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.content.VocabularyItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VocabularyItemRepository extends JpaRepository<VocabularyItem, UUID> {
    @Query("""
        SELECT v
        FROM VocabularyItem v
        WHERE (:category IS NULL OR v.category = :category)
          AND (
            :query IS NULL
            OR LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(v.definition) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(v.example) LIKE LOWER(CONCAT('%', :query, '%'))
          )
        """)
    Page<VocabularyItem> searchVocabulary(
        @Param("query") String query,
        @Param("category") String category,
        Pageable pageable
    );

    @Query("""
        SELECT v.category AS category, COUNT(v) AS count
        FROM VocabularyItem v
        GROUP BY v.category
        """)
    List<VocabularyCategoryCount> countByCategory();

    interface VocabularyCategoryCount {
        String getCategory();
        long getCount();
    }
}
