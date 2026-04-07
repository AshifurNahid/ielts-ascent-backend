package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WritingSuggestionRepository extends JpaRepository<WritingSuggestion, Long> {
    List<WritingSuggestion> findBySubmissionId(Long submissionId);

    List<WritingSuggestion> findBySubmissionIdIn(List<Long> submissionIds);

    void deleteBySubmissionId(Long submissionId);
}
