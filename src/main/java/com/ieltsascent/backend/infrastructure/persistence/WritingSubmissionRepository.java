package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.WritingSubmission;
import com.ieltsascent.backend.domain.practice.WritingSubmissionStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface WritingSubmissionRepository extends JpaRepository<WritingSubmission, UUID> {
    Optional<WritingSubmission> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);

    List<WritingSubmission> findTop10ByUserIdOrderByCreatedAtDesc(UUID userId);
    List<WritingSubmission> findTop20ByUserIdAndEvaluationResultIsNotNullOrderByCreatedAtDesc(UUID userId);

    Optional<WritingSubmission> findByIdAndUserId(UUID id, UUID userId);

    List<WritingSubmission> findByStatus(WritingSubmissionStatus status, Pageable pageable);

    @Query("""
        select submission
        from WritingSubmission submission
        join fetch submission.evaluationResult evaluation
        join submission.prompt prompt
        where submission.user.id = :userId
          and prompt.taskType = :taskType
          and submission.evaluationResult is not null
        order by evaluation.evaluatedAt desc
        """)
    List<WritingSubmission> findRecentEvaluations(
        @Param("userId") UUID userId,
        @Param("taskType") String taskType,
        Pageable pageable
    );
}
