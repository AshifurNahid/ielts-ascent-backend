package com.ieltsascent.backend.application.studyplan.dto;

import com.ieltsascent.backend.domain.studyplan.StudyTask;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyTaskItemDto {
    private final Long id;
    private final String title;
    private final String taskType;
    private final Integer estimatedMinutes;

    public static StudyTaskItemDto from(StudyTask task) {
        return new StudyTaskItemDto(task.getId(), task.getTitle(), task.getTaskType(), task.getEstimatedMinutes());
    }
}
