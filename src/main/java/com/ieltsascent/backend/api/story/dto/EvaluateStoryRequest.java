package com.ieltsascent.backend.api.story.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record EvaluateStoryRequest(@NotNull UUID submissionId) {
}
