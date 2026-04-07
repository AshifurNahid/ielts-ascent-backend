package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakingRecordingRepository extends JpaRepository<SpeakingRecordingMetadata, Long> {
    Optional<SpeakingRecordingMetadata> findFirstByUserIdOrderByCreatedAtDesc(Long userId);

    List<SpeakingRecordingMetadata> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
    List<SpeakingRecordingMetadata> findTop20ByUserIdAndEvaluationResultIsNotNullOrderByCreatedAtDesc(Long userId);
}
