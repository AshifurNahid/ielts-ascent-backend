package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.WritingEvaluationResult;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingEvaluationResultRepository extends JpaRepository<WritingEvaluationResult, UUID> {
    Optional<WritingEvaluationResult> findBySubmissionId(UUID submissionId);
}
