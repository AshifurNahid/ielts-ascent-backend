package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.ReadingRecommendationLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRecommendationLogRepository extends JpaRepository<ReadingRecommendationLog, UUID> {
}
