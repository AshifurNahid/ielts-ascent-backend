package com.ieltsascent.backend.api.story.dto;

import java.time.Instant;

public record StoryHistoryResponse(
    Long submissionId,
    String topic,
    Integer wordCount,
    Instant submittedAt,
    Double overallBand
) {
}
