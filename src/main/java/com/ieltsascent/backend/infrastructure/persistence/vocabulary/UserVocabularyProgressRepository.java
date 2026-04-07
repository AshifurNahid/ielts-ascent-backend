package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.UserVocabularyProgress;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserVocabularyProgressRepository extends JpaRepository<UserVocabularyProgress, Long> {
    Optional<UserVocabularyProgress> findByUserIdAndVocabularyWordId(Long userId, Long vocabularyWordId);

    List<UserVocabularyProgress> findByUserId(Long userId);

    @Query("""
        SELECT p
        FROM UserVocabularyProgress p
        WHERE p.userId = :userId
          AND p.vocabularyWord.id IN :wordIds
        """)
    List<UserVocabularyProgress> findByUserIdAndWordIds(
        @Param("userId") Long userId,
        @Param("wordIds") Collection<Long> wordIds
    );

    long countByUserIdAndMasteryLevelGreaterThanEqual(Long userId, Double masteryLevel);

    long countByUserIdAndMarkedDifficultTrue(Long userId);

    long countByUserIdAndLastPracticedAtAfter(Long userId, Instant since);
}
