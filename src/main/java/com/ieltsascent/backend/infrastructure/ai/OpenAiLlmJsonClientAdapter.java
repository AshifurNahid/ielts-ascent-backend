package com.ieltsascent.backend.infrastructure.ai;

import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.application.ai.LlmJsonResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class OpenAiLlmJsonClientAdapter implements LlmJsonClientPort {
    private final WebClient webClient;
    private final LlmWebClientSupport support;
    private final AiInteractionLogger interactionLogger;

    public OpenAiLlmJsonClientAdapter(
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
        OpenAiRequest payload = new OpenAiRequest(
            request.model(),
            request.temperature(),
            List.of(
                new Message("system", request.systemPrompt()),
                new Message("user", request.userPrompt())
            ),
            Map.of(
                "type", "json_schema",
                "json_schema", Map.of("name", "writing_eval", "schema", request.jsonSchema())
            )
        );
        OpenAiResponse response = support.post(
            "/v1/chat/completions",
            payload,
            OpenAiResponse.class,
            request.correlationId()
        );
        String content = response == null || response.choices() == null || response.choices().isEmpty()
            ? null
            : response.choices().getFirst().message().content();
        Integer tokenUsage = response == null || response.usage() == null ? null : response.usage().totalTokens();
        long latencyMs = Duration.between(start, Instant.now()).toMillis();
        LlmJsonResponse result = new LlmJsonResponse(content, "openai", request.model(), latencyMs, tokenUsage);
        interactionLogger.log(request, "openai", request.model(), latencyMs, tokenUsage, content);
        return result;
    }

    private record OpenAiRequest(
        String model,
        double temperature,
        List<Message> messages,
        Map<String, Object> responseFormat
    ) {
    }

    private record Message(String role, String content) {
    }

    private record OpenAiResponse(List<Choice> choices, Usage usage) {
    }

    private record Choice(Message message) {
    }

    private record Usage(Integer totalTokens) {
    }
}
