package com.ieltsascent.backend.api.story.dto;

import java.time.LocalDateTime;

public record StoryHistoryResponse(
    Long submissionId,
    String topic,
    Integer wordCount,
    LocalDateTime submittedAt,
    Double overallBand
) {
}
