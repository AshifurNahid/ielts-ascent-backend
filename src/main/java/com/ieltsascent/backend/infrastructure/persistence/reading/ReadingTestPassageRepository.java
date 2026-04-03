package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingTestPassage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingTestPassageRepository extends JpaRepository<ReadingTestPassage, UUID> {
    List<ReadingTestPassage> findByReadingTestIdOrderByOrderIndexAsc(UUID readingTestId);
    void deleteByReadingTestId(UUID readingTestId);
}
