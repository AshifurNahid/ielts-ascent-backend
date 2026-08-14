package com.ieltsascent.backend.application.practice.dto;

import com.ieltsascent.backend.domain.practice.PracticeSession;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PracticeSessionItemDto {
    private final Long sessionId;
    private final String skillType;
    private final Long taskId;
    private final Instant startedAt;
    private final Instant completedAt;

    public static PracticeSessionItemDto from(PracticeSession session) {
        return new PracticeSessionItemDto(
            session.getId(),
            session.getSkillType(),
            session.getTaskId(),
            session.getStartedAt(),
            session.getCompletedAt()
        );
    }
}

