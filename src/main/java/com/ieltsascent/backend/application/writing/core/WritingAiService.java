package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.domain.writing.WritingTaskType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class WritingAiService {
    private static final String DEFAULT_SUMMARY = "Evaluation completed.";
    private static final String SYSTEM_EVALUATOR_PROMPT = """
        You are an IELTS Writing examiner.
        Evaluate only from the provided essay text.
        Do not invent facts and do not inflate scores.
        """;

    private static final String USER_EVALUATION_TEMPLATE = """
        Evaluate this IELTS essay and return STRICT JSON only.

        SCORING RULES:
        - Score range: 0.0 to 9.0
        - Step size: 0.5 only
        - Criteria: Task Response, Coherence & Cohesion, Lexical Resource, Grammar

        OUTPUT JSON SCHEMA:
        {
          "overallBand": number,
          "taskResponseBand": number,
          "coherenceBand": number,
          "lexicalBand": number,
          "grammarBand": number,
          "confidence": number,
          "strengths": ["string"],
          "weakPoints": [
            {
              "category": "task_response|coherence|lexical_resource|grammar",
              "weakKey": "string",
              "severity": "low|medium|high",
              "explanation": "string",
              "suggestion": "string"
            }
          ],
          "suggestions": [
            {
              "suggestionType": "string",
              "originalText": "string",
              "suggestedText": "string",
              "explanation": "string"
            }
          ],
          "summary": "string"
        }

        CONTEXT:
        - taskType: %s
        - prompt: %s
        - essay: %s
        - userHistorySummary: %s

        Return JSON only. No markdown fences. No extra text.
        """;

    private static final String SYSTEM_IMPROVE_PROMPT = """
        You are an IELTS writing coach.
        Improve only the provided section while preserving the writer's intent.
        Keep edits practical and exam-appropriate.
        """;

    private static final String USER_IMPROVE_TEMPLATE = """
        Improve one section of an IELTS essay.
        Return plain text with:
        1) improved section
        2) short explanation (1-2 sentences)

        Prompt: %s
        Essay: %s
        Section: %s
        Instruction: %s
        """;

    private static final int MAX_ATTEMPTS = 2;

    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${ai.temperature:0.1}")
    private double temperature;

    public AiEvaluationResponse evaluateEssay(WritingTaskType taskType, String promptText, String essayText, String userHistorySummary) {
        String userPrompt = USER_EVALUATION_TEMPLATE.formatted(
            taskType,
            safe(promptText),
            safe(essayText),
            safe(userHistorySummary)
        );
        try {
            String content = requestWithRetry(SYSTEM_EVALUATOR_PROMPT, userPrompt);
            return parseEvaluation(content);
        } catch (Exception ex) {
            log.warn("Writing AI evaluation failed after retries", ex);
            return AiEvaluationResponse.failed();
        }
    }

    private AiEvaluationResponse parseEvaluation(String content) {
        if (content == null || content.isBlank()) {
            return AiEvaluationResponse.failed();
        }
        JsonNode root = objectMapper.readTree(stripMarkdownCodeFence(content));
        return new AiEvaluationResponse(
            normalizedBand(root, "overallBand"),
            normalizedBand(root, "taskResponseBand"),
            normalizedBand(root, "coherenceBand"),
            normalizedBand(root, "lexicalBand"),
            normalizedBand(root, "grammarBand"),
            root.path("confidence").asDouble(0.0),
            toStringList(root.path("strengths")),
            toWeakPoints(root.path("weakPoints")),
            toSuggestions(root.path("suggestions")),
            summaryText(root.path("summary"))
        );
    }

    private Double normalizedBand(JsonNode root, String field) {
        JsonNode node = root.path(field);
        if (!node.isNumber()) {
            return null;
        }
        double raw = node.asDouble();
        double clamped = Math.clamp(raw, 0.0, 9.0);
        return Math.round(clamped * 2.0) / 2.0;
    }

    private String summaryText(JsonNode node) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return DEFAULT_SUMMARY;
        }
        try {
            String value = objectMapper.convertValue(node, String.class);
            return value == null || value.isBlank() ? DEFAULT_SUMMARY : value;
        } catch (IllegalArgumentException _) {
            return DEFAULT_SUMMARY;
        }
    }

    public String improveSection(String promptText, String essayText, String sectionText, String instruction) {
        String userPrompt = USER_IMPROVE_TEMPLATE.formatted(
            safe(promptText),
            safe(essayText),
            safe(sectionText),
            safe(instruction)
        );
        try {
            return requestWithRetry(SYSTEM_IMPROVE_PROMPT, userPrompt);
        } catch (Exception ex) {
            log.warn("Writing section improvement failed after retries", ex);
            return sectionText;
        }
    }

    private String requestWithRetry(String systemPrompt, String userPrompt) {
        Exception last = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                log.debug("Writing AI call attempt {} with configured temperature {}", attempt, temperature);
                String content = chatClientBuilder
                    .build()
                    .prompt()
                    .system(systemPrompt)
                    .user(userPrompt)
                    .call()
                    .content();
                if (content != null && !content.isBlank()) {
                    return content;
                }
            } catch (Exception ex) {
                last = ex;
                log.warn("Writing AI call attempt {} failed", attempt, ex);
            }
        }
        throw new IllegalStateException("Writing AI call failed after retries", last);
    }

    private List<String> toStringList(JsonNode node) {
        if (!node.isArray()) {
            return List.of();
        }
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }

    private List<AiWeakPoint> toWeakPoints(JsonNode node) {
        if (!node.isArray()) {
            return List.of();
        }
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, AiWeakPoint.class));
    }

    private List<AiSuggestion> toSuggestions(JsonNode node) {
        if (!node.isArray()) {
            return List.of();
        }
        return objectMapper.convertValue(node, objectMapper.getTypeFactory().constructCollectionType(List.class, AiSuggestion.class));
    }

    private String stripMarkdownCodeFence(String raw) {
        String value = raw.trim();
        if (value.startsWith("```") && value.endsWith("```")) {
            value = value.substring(3, value.length() - 3).trim();
            if (value.startsWith("json")) {
                value = value.substring(4).trim();
            }
        }
        return value;
    }

    private String safe(String text) {
        return text == null ? "" : text;
    }

    public record AiEvaluationResponse(
        Double overallBand,
        Double taskResponseBand,
        Double coherenceBand,
        Double lexicalBand,
        Double grammarBand,
        Double confidence,
        List<String> strengths,
        List<AiWeakPoint> weakPoints,
        List<AiSuggestion> suggestions,
        String summary
    ) {
        public static AiEvaluationResponse failed() {
            return new AiEvaluationResponse(null, null, null, null, null, 0.0, List.of(), List.of(), List.of(), "Evaluation pending");
        }
    }

    public record AiWeakPoint(
        String category,
        String weakKey,
        String severity,
        String explanation,
        String suggestion
    ) {
    }

    public record AiSuggestion(
        String suggestionType,
        String originalText,
        String suggestedText,
        String explanation
    ) {
    }
}
