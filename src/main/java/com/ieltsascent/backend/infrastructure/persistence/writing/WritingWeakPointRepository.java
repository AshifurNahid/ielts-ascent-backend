package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingWeakPointRepository extends JpaRepository<WritingWeakPoint, UUID> {
    List<WritingWeakPoint> findTop100ByUserIdOrderByCreatedAtDesc(UUID userId);

    List<WritingWeakPoint> findBySubmissionId(UUID submissionId);
}
