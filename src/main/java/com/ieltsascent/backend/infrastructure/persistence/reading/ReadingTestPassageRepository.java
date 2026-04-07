package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingTestPassage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingTestPassageRepository extends JpaRepository<ReadingTestPassage, Long> {
    List<ReadingTestPassage> findByReadingTestIdOrderByOrderIndexAsc(Long readingTestId);
    void deleteByReadingTestId(Long readingTestId);
}
