package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.studyplan.StudyTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    List<StudyTask> findByStudyPlanWeekId(Long weekId);
}
