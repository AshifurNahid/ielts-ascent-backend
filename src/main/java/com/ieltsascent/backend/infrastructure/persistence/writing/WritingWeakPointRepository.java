package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingWeakPointRepository extends JpaRepository<WritingWeakPoint, Long> {
    List<WritingWeakPoint> findTop100ByUserIdOrderByCreatedAtDesc(Long userId);

    List<WritingWeakPoint> findBySubmissionId(Long submissionId);

    List<WritingWeakPoint> findBySubmissionIdIn(List<Long> submissionIds);

    void deleteBySubmissionId(Long submissionId);
}
