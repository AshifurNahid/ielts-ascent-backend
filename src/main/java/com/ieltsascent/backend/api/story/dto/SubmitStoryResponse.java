package com.ieltsascent.backend.api.story.dto;

import java.util.UUID;

public record SubmitStoryResponse(UUID submissionId, Integer wordCount) {
}
