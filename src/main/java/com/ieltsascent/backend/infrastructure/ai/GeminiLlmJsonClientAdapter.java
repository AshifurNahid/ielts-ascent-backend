package com.ieltsascent.backend.infrastructure.ai;

import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.application.ai.LlmJsonResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class GeminiLlmJsonClientAdapter implements LlmJsonClientPort {
    private final WebClient webClient;
    private final LlmWebClientSupport support;
    private final AiInteractionLogger interactionLogger;

    public GeminiLlmJsonClientAdapter(
        WritingAiProperties.Provider provider,
        int timeoutMs,
        int maxRetries,
        AiInteractionLogger interactionLogger
    ) {
        this.webClient = WebClient.builder()
            .baseUrl(provider.getBaseUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + provider.getApiKey())
            .build();
        this.support = new LlmWebClientSupport(webClient, timeoutMs, maxRetries);
        this.interactionLogger = interactionLogger;
    }

    @Override
    public LlmJsonResponse generateStrictJson(LlmJsonRequest request) {
        Instant start = Instant.now();
        String prompt = request.systemPrompt() + "\n" + request.userPrompt()
            + "\nReturn ONLY JSON that matches this schema:\n" + request.jsonSchema();
        GeminiRequest payload = new GeminiRequest(
            List.of(new Content(List.of(new Part(prompt)))),
            new GenerationConfig(request.temperature())
        );
        GeminiResponse response = support.post(
            "/v1/models/" + request.model() + ":generateContent",
            payload,
            GeminiResponse.class,
            request.correlationId()
        );
        String content = null;
        Integer tokenUsage = null;
        if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
            Content contentNode = response.candidates().getFirst().content();
            if (contentNode != null && contentNode.parts() != null && !contentNode.parts().isEmpty()) {
                content = contentNode.parts().getFirst().text();
            }
        }
        if (response != null && response.usageMetadata() != null) {
            tokenUsage = response.usageMetadata().totalTokenCount();
        }
        long latencyMs = Duration.between(start, Instant.now()).toMillis();
        LlmJsonResponse result = new LlmJsonResponse(content, "gemini", request.model(), latencyMs, tokenUsage);
        interactionLogger.log(request, "gemini", request.model(), latencyMs, tokenUsage, content);
        return result;
    }

    private record GeminiRequest(List<Content> contents, GenerationConfig generationConfig) {
    }

    private record Content(List<Part> parts) {
    }

    private record Part(String text) {
    }

    private record GenerationConfig(double temperature) {
    }

    private record GeminiResponse(List<Candidate> candidates, UsageMetadata usageMetadata) {
    }

    private record Candidate(Content content) {
    }

    private record UsageMetadata(Integer totalTokenCount) {
    }
}
