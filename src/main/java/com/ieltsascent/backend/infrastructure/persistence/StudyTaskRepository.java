package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.studyplan.StudyTask;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTaskRepository extends JpaRepository<StudyTask, UUID> {
    List<StudyTask> findByStudyPlanWeekId(UUID weekId);
}
