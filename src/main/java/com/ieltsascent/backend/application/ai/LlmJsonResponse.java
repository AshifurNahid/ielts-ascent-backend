package com.ieltsascent.backend.application.ai;

public record LlmJsonResponse(
    String content,
    String provider,
    String model,
    long latencyMs,
    Integer tokenUsage
) {
}
