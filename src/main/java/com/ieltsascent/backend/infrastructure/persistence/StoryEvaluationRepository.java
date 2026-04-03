package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.story.StoryEvaluation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryEvaluationRepository extends JpaRepository<StoryEvaluation, UUID> {
    Optional<StoryEvaluation> findBySubmissionId(UUID submissionId);

    List<StoryEvaluation> findBySubmissionUserIdOrderByCreatedAtDesc(UUID userId);
}
