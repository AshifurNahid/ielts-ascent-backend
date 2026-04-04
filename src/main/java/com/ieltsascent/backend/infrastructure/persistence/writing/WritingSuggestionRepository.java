package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingSuggestionRepository extends JpaRepository<WritingSuggestion, UUID> {
    List<WritingSuggestion> findBySubmissionId(UUID submissionId);

    List<WritingSuggestion> findBySubmissionIdIn(List<UUID> submissionIds);

    void deleteBySubmissionId(UUID submissionId);
}
