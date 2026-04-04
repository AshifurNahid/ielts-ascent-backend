package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingSubmissionRepository extends JpaRepository<WritingSubmission, UUID> {
    Optional<WritingSubmission> findByIdAndUserId(UUID id, UUID userId);

    Optional<WritingSubmission> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);

    Page<WritingSubmission> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    List<WritingSubmission> findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(UUID userId, WritingSubmissionStatus status);

    long countByUserIdAndStatus(UUID userId, WritingSubmissionStatus status);

    List<WritingSubmission> findByUserIdAndStatusAndSubmittedAtAfterOrderBySubmittedAtAsc(
        UUID userId,
        WritingSubmissionStatus status,
        Instant submittedAfter
    );
}
