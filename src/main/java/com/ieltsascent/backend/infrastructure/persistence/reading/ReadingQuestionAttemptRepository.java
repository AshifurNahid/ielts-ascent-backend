package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingQuestionAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingQuestionAttemptRepository extends JpaRepository<ReadingQuestionAttempt, Long> {
    List<ReadingQuestionAttempt> findByReadingAttemptId(Long readingAttemptId);
    List<ReadingQuestionAttempt> findByReadingAttemptIdOrderByCreatedAtAsc(Long readingAttemptId);
}
