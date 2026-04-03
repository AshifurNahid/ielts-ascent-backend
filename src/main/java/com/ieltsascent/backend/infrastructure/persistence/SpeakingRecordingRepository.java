package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakingRecordingRepository extends JpaRepository<SpeakingRecordingMetadata, UUID> {
    Optional<SpeakingRecordingMetadata> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);

    List<SpeakingRecordingMetadata> findTop10ByUserIdOrderByCreatedAtDesc(UUID userId);
    List<SpeakingRecordingMetadata> findTop20ByUserIdAndEvaluationResultIsNotNullOrderByCreatedAtDesc(UUID userId);
}
