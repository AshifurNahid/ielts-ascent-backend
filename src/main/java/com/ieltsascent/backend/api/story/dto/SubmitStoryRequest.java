package com.ieltsascent.backend.api.story.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record SubmitStoryRequest(@NotBlank String topic, @NotNull Map<String, String> parts) {
}
