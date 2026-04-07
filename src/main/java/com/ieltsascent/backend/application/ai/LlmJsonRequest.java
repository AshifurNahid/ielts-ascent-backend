package com.ieltsascent.backend.application.ai;

public record LlmJsonRequest(
    String systemPrompt,
    String userPrompt,
    String jsonSchema,
    String model,
    double temperature,
    String correlationId,
    Long userId,
    String promptName,
    String promptVersion
) {
}
