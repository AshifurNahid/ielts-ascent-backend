package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.story.StorySubmission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorySubmissionRepository extends JpaRepository<StorySubmission, Long> {
    Optional<StorySubmission> findByIdAndUserId(Long id, Long userId);

    List<StorySubmission> findByUserIdOrderByCreatedAtDesc(Long userId);
}
