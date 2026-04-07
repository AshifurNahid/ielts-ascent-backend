package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingSubmissionRepository extends JpaRepository<WritingSubmission, Long> {
    Optional<WritingSubmission> findByIdAndUserId(Long id, Long userId);

    Optional<WritingSubmission> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    Page<WritingSubmission> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    List<WritingSubmission> findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(Long userId, WritingSubmissionStatus status);

    long countByUserIdAndStatus(Long userId, WritingSubmissionStatus status);

    List<WritingSubmission> findByUserIdAndStatusAndSubmittedAtAfterOrderBySubmittedAtAsc(
        Long userId,
        WritingSubmissionStatus status,
        Instant submittedAfter
    );
}
