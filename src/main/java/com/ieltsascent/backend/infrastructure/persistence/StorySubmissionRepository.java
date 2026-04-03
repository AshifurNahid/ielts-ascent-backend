package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.story.StorySubmission;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorySubmissionRepository extends JpaRepository<StorySubmission, UUID> {
    Optional<StorySubmission> findByIdAndUserId(UUID id, UUID userId);

    List<StorySubmission> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
