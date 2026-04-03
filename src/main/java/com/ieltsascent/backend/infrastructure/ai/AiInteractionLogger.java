package com.ieltsascent.backend.infrastructure.ai;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.domain.ai.AiInteractionLog;
import com.ieltsascent.backend.infrastructure.persistence.AiInteractionLogRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AiInteractionLogger {
    private final AiInteractionLogRepository repository;
    private final ObjectMapper objectMapper;

    public AiInteractionLogger(AiInteractionLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void log(
        LlmJsonRequest request,
        String provider,
        String model,
        long latencyMs,
        Integer tokenUsage,
        String response
    ) {
        AiInteractionLog log = new AiInteractionLog();
        log.setUserId(request.userId());
        log.setCorrelationId(request.correlationId());
        log.setProvider(provider);
        log.setModel(model);
        log.setPromptName(request.promptName());
        log.setPromptVersion(request.promptVersion());
        log.setLatencyMs((int) latencyMs);
        log.setTokenUsage(tokenUsage);
        log.setRequestRedacted(redactRequest(request));
        log.setResponseRedacted(redactResponse(response));
        repository.save(log);
    }

    private String redactRequest(LlmJsonRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("systemPrompt", request.systemPrompt());
        payload.put("userPrompt", redactEssay(request.userPrompt()));
        payload.put("jsonSchema", "[REDACTED_SCHEMA]");
        payload.put("model", request.model());
        payload.put("temperature", request.temperature());
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JacksonException ex) {
            return "{\"error\":\"unable to serialize request\"}";
        }
    }

    private String redactResponse(String response) {
        if (response == null) {
            return null;
        }
        return response.length() > 4000 ? response.substring(0, 4000) + "..." : response;
    }

    private String redactEssay(String prompt) {
        if (prompt == null) {
            return null;
        }
        int essayIndex = prompt.indexOf("Essay:");
        if (essayIndex < 0) {
            return prompt;
        }
        return prompt.substring(0, essayIndex + 6) + " [REDACTED]";
    }
}
