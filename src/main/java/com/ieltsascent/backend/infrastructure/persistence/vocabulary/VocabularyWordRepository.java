package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.VocabularyLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyStatus;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VocabularyWordRepository extends JpaRepository<VocabularyWord, Long> {
    @EntityGraph(attributePaths = { "examples", "tags", "synonyms", "antonyms", "collocations" })
    @Query("""
        SELECT DISTINCT w
        FROM VocabularyWord w
        LEFT JOIN w.tags t
        WHERE (:status IS NULL OR w.status = :status)
          AND (:level IS NULL OR w.level = :level)
          AND (:premium IS NULL OR w.premium = :premium)
          AND (:query IS NULL OR LOWER(w.word) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(w.simpleMeaning) LIKE LOWER(CONCAT('%', :query, '%')))
          AND (:tag IS NULL OR t = :tag)
        """)
    Page<VocabularyWord> searchAdmin(
        @Param("status") VocabularyStatus status,
        @Param("level") VocabularyLevel level,
        @Param("premium") Boolean premium,
        @Param("tag") String tag,
        @Param("query") String query,
        Pageable pageable
    );

    @EntityGraph(attributePaths = { "examples", "tags", "synonyms", "antonyms", "collocations" })
    @Query("""
        SELECT DISTINCT w
        FROM VocabularyWord w
        LEFT JOIN w.tags t
        WHERE w.status = com.ieltsascent.backend.domain.vocabulary.VocabularyStatus.PUBLISHED
          AND (:premiumUser = TRUE OR w.premium = FALSE)
          AND (:levels IS NULL OR w.level IN :levels)
          AND (:minBand IS NULL OR w.ieltsBandMax >= :minBand)
          AND (:maxBand IS NULL OR w.ieltsBandMin <= :maxBand)
        """)
    List<VocabularyWord> findCandidates(
        @Param("premiumUser") boolean premiumUser,
        @Param("levels") Collection<VocabularyLevel> levels,
        @Param("minBand") Double minBand,
        @Param("maxBand") Double maxBand
    );

    @EntityGraph(attributePaths = { "examples", "tags", "synonyms", "antonyms", "collocations" })
    @Query("""
        SELECT w
        FROM VocabularyWord w
        WHERE w.id = :id
        """)
    java.util.Optional<VocabularyWord> findDetailedById(@Param("id") Long id);
}
