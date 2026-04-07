package com.ieltsascent.backend.api.story.dto;

import jakarta.validation.constraints.NotNull;

public record EvaluateStoryRequest(@NotNull Long submissionId) {
}
