package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.story.StoryEvaluation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryEvaluationRepository extends JpaRepository<StoryEvaluation, Long> {
    Optional<StoryEvaluation> findBySubmissionId(Long submissionId);

    List<StoryEvaluation> findBySubmissionUserIdOrderByCreatedAtDesc(Long userId);
}
