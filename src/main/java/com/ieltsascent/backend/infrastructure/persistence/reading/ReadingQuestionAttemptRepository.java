package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingQuestionAttempt;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingQuestionAttemptRepository extends JpaRepository<ReadingQuestionAttempt, UUID> {
    List<ReadingQuestionAttempt> findByReadingAttemptId(UUID readingAttemptId);
    List<ReadingQuestionAttempt> findByReadingAttemptIdOrderByCreatedAtAsc(UUID readingAttemptId);
}
