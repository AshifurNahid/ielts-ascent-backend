package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.UserVocabularyProgress;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserVocabularyProgressRepository extends JpaRepository<UserVocabularyProgress, UUID> {
    Optional<UserVocabularyProgress> findByUserIdAndVocabularyWordId(UUID userId, UUID vocabularyWordId);

    List<UserVocabularyProgress> findByUserId(UUID userId);

    @Query("""
        SELECT p
        FROM UserVocabularyProgress p
        WHERE p.userId = :userId
          AND p.vocabularyWord.id IN :wordIds
        """)
    List<UserVocabularyProgress> findByUserIdAndWordIds(
        @Param("userId") UUID userId,
        @Param("wordIds") Collection<UUID> wordIds
    );

    long countByUserIdAndMasteryLevelGreaterThanEqual(UUID userId, Double masteryLevel);

    long countByUserIdAndMarkedDifficultTrue(UUID userId);

    long countByUserIdAndLastPracticedAtAfter(UUID userId, Instant since);
}
