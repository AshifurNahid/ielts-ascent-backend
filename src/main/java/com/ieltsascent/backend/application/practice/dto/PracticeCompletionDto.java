package com.ieltsascent.backend.application.practice.dto;

import com.ieltsascent.backend.domain.practice.PracticeSession;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PracticeCompletionDto {
    private final Long sessionId;
    private final Long taskId;
    private final String completedAt;

    public static PracticeCompletionDto from(PracticeSession session) {
        return new PracticeCompletionDto(session.getId(), session.getTaskId(), session.getCompletedAt().toString());
    }
}

