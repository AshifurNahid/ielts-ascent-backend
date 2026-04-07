package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.studyplan.StudyPlan;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    Optional<StudyPlan> findFirstByUserIdOrderByCreatedAtDesc(Long userId);
}
