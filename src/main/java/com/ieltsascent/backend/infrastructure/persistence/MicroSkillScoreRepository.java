package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicroSkillScoreRepository extends JpaRepository<MicroSkillScore, UUID> {
    List<MicroSkillScore> findByDiagnosticResultId(UUID diagnosticResultId);

    List<MicroSkillScore> findByDiagnosticResultSessionUserIdOrderByCreatedAtDesc(UUID userId);
}
