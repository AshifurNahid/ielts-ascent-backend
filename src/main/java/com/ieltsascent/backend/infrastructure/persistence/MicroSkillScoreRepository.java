package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicroSkillScoreRepository extends JpaRepository<MicroSkillScore, Long> {
    List<MicroSkillScore> findByDiagnosticResultId(Long diagnosticResultId);

    List<MicroSkillScore> findByDiagnosticResultSessionUserIdOrderByCreatedAtDesc(Long userId);
}
