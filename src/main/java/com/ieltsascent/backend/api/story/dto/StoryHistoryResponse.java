package com.ieltsascent.backend.api.story.dto;

import java.time.Instant;
import java.util.UUID;

public record StoryHistoryResponse(
    UUID submissionId,
    String topic,
    Integer wordCount,
    Instant submittedAt,
    Double overallBand
) {
}
