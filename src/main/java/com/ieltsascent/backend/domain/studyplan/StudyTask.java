package com.ieltsascent.backend.domain.studyplan;

import com.ieltsascent.backend.domain.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class StudyTask extends BaseEntity {
    @ManyToOne
    private StudyPlanWeek studyPlanWeek;

    private String taskType;

    private String title;

    private Integer estimatedMinutes;

    public StudyPlanWeek getStudyPlanWeek() {
        return studyPlanWeek;
    }

    public void setStudyPlanWeek(StudyPlanWeek studyPlanWeek) {
        this.studyPlanWeek = studyPlanWeek;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }
}
