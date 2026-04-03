package com.ieltsascent.backend.application.story;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.application.ai.LlmJsonResponse;
import com.ieltsascent.backend.infrastructure.ai.WritingAiProperties;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoryEvaluationEngine {
    private static final String EVAL_SCHEMA = """
        {
          "type": "object",
          "required": ["fluency", "coherence", "vocabulary", "grammar", "strengths", "improvements"],
          "properties": {
            "fluency": {"type": "number"},
            "coherence": {"type": "number"},
            "vocabulary": {"type": "number"},
            "grammar": {"type": "number"},
            "strengths": {"type": "array", "items": {"type": "string"}, "minItems": 2},
            "improvements": {"type": "array", "items": {"type": "string"}, "minItems": 2}
          }
        }
        """;

    private final LlmJsonClientPort llmJsonClientPort;
    private final WritingAiProperties properties;
    private final ObjectMapper objectMapper;

    public StoryEvaluationResult evaluate(UUID userId, String topic, String storyText) {
        try {
            if (storyText == null || storyText.isBlank()) {
                return fallback("Story is empty. Add more content for a useful evaluation.");
            }
            LlmJsonResponse response = llmJsonClientPort.generateStrictJson(new LlmJsonRequest(
                "You are an IELTS Speaking Part 2 examiner. Return JSON only.",
                buildUserPrompt(topic, storyText),
                EVAL_SCHEMA,
                properties.getModel().getWriting(),
                Math.max(0.1, properties.getTemperature()),
                UUID.randomUUID().toString(),
                userId,
                "story_builder_eval",
                "v1"
            ));
            JsonNode node = objectMapper.readTree(response.content());
            return normalize(node);
        } catch (Exception ex) {
            return fallback("AI evaluation unavailable, using fallback scoring.");
        }
    }

    private String buildUserPrompt(String topic, String storyText) {
        return """
            Topic: %s

            Story:
            %s

            Evaluate Fluency, Coherence, Vocabulary, Grammar on IELTS 0-9 scale.
            Provide exactly 2 strengths and 2 improvements.
            """.formatted(topic == null ? "" : topic, storyText);
    }

    private StoryEvaluationResult normalize(JsonNode node) {
        double fluency = clamp(node.path("fluency").asDouble(6.0));
        double coherence = clamp(node.path("coherence").asDouble(6.0));
        double vocabulary = clamp(node.path("vocabulary").asDouble(6.0));
        double grammar = clamp(node.path("grammar").asDouble(6.0));
        double overall = roundHalf((fluency + coherence + vocabulary + grammar) / 4.0);
        List<String> strengths = extractList(node.path("strengths"), List.of("Clear narrative flow", "Relevant content"));
        List<String> improvements = extractList(
            node.path("improvements"),
            List.of("Add richer vocabulary", "Use wider grammar structures")
        );
        return new StoryEvaluationResult(fluency, coherence, vocabulary, grammar, overall, strengths, improvements);
    }

    private StoryEvaluationResult fallback(String note) {
        return new StoryEvaluationResult(
            6.0,
            6.0,
            6.0,
            6.0,
            6.0,
            List.of("Story structure is understandable", "Main idea is communicated"),
            List.of("Add specific examples", note)
        );
    }

    private List<String> extractList(JsonNode node, List<String> fallback) {
        if (!node.isArray() || node.isEmpty()) {
            return fallback;
        }
        List<String> values = new java.util.ArrayList<>();
        node.forEach(item -> {
            if (item.isTextual()) {
                String value = item.asText().trim();
                if (!value.isBlank()) {
                    values.add(value);
                }
            }
        });
        if (values.isEmpty()) {
            return fallback;
        }
        return values.stream().limit(2).toList();
    }

    private double clamp(double value) {
        if (value < 0) {
            return 0;
        }
        if (value > 9) {
            return 9;
        }
        return roundHalf(value);
    }

    private double roundHalf(double value) {
        return Math.round(value * 2.0) / 2.0;
    }

    public record StoryEvaluationResult(
        double fluency,
        double coherence,
        double vocabulary,
        double grammar,
        double overallBand,
        List<String> strengths,
        List<String> improvements
    ) {
    }
}
