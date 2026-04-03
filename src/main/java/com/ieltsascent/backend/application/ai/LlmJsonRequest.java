package com.ieltsascent.backend.application.ai;

import java.util.UUID;

public record LlmJsonRequest(
    String systemPrompt,
    String userPrompt,
    String jsonSchema,
    String model,
    double temperature,
    String correlationId,
    UUID userId,
    String promptName,
    String promptVersion
) {
}
