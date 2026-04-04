package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.readingtest.ReadingQuestion;
import com.ieltsascent.backend.infrastructure.persistence.reading.ReadingQuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ReadingAiService {
    private static final String SYSTEM_EXPLAIN_PROMPT = """
        You are an IELTS Reading evaluator assistant.
        Explain based only on provided passage/question data.
        Do not invent facts or hidden clues.
        """;

    private static final String USER_EXPLAIN_TEMPLATE = """
        Return STRICT JSON only with this schema:
        {
          "correctAnswerExplanation": "string",
          "wrongAnswerReason": "string",
          "strategyTip": "string"
        }

        RULES:
        - Keep each field concise and practical.
        - Use passage evidence only.
        - Do not output markdown/code fences.

        INPUT:
        Passage: {passage}
        Question: {question}
        Correct answer: {correctAnswer}
        User answer: {userAnswer}
        Question type: {questionType}
        """;

    private static final String SYSTEM_ADMIN_PROMPT = """
        You are an IELTS Reading content assistant for admin draft enrichment.
        Generate reviewer-friendly draft artifacts only.
        Do not claim absolute certainty.
        """;

    private static final String USER_ADMIN_TEMPLATE = """
        Return STRICT JSON only with this schema:
        {
          "draftExplanation": "string",
          "answerSourceHint": "string",
          "learnerTip": "string",
          "draftDistractors": ["string", "string", "string"],
          "shortSummary": "string"
        }

        RULES:
        - Distractors must be plausible but incorrect.
        - Keep output concise.
        - Do not output markdown/code fences.

        INPUT:
        Passage: {passage}
        Question: {question}
        Correct answer: {correctAnswer}
        Question type: {questionType}
        """;

    private static final int MAX_ATTEMPTS = 2;

    private final ChatClient.Builder chatClientBuilder;
    private final ReadingQuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    @Value("${ai.temperature:0.1}")
    private double temperature;

    public ReadingDtos.ReadingAiExplainResponse explain(ReadingDtos.ReadingAiExplainRequest request) {
        ReadingQuestion q = questionRepository.findById(request.questionId()).orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        String content = requestWithRetry(
            SYSTEM_EXPLAIN_PROMPT,
            USER_EXPLAIN_TEMPLATE,
            Map.of(
                "passage", safe(q.getPassage().getContent()),
                "question", safe(q.getPrompt()),
                "correctAnswer", safe(q.getCorrectAnswer()),
                "userAnswer", safe(request.userAnswer()).isBlank() ? "(empty)" : safe(request.userAnswer()),
                "questionType", q.getType().name()
            )
        );
        ExplainPayload payload = parseExplainPayload(content);

        if (payload == null || isBlank(payload.correctAnswerExplanation()) || isBlank(payload.wrongAnswerReason()) || isBlank(payload.strategyTip())) {
            throw new IllegalArgumentException("AI explanation payload is invalid");
        }

        return new ReadingDtos.ReadingAiExplainResponse(payload.correctAnswerExplanation(), payload.wrongAnswerReason(), payload.strategyTip());
    }

    public ReadingDtos.AdminAiEnrichResponse enrich(ReadingDtos.AdminAiEnrichRequest request) {
        String content = requestWithRetry(
            SYSTEM_ADMIN_PROMPT,
            USER_ADMIN_TEMPLATE,
            Map.of(
                "passage", safe(request.passageContent()),
                "question", safe(request.questionPrompt()),
                "correctAnswer", safe(request.correctAnswer()),
                "questionType", request.questionType().name()
            )
        );
        EnrichPayload payload = parseEnrichPayload(content);

        if (payload == null || isBlank(payload.draftExplanation()) || isBlank(payload.answerSourceHint()) || isBlank(payload.learnerTip())
            || isBlank(payload.shortSummary()) || payload.draftDistractors() == null || payload.draftDistractors().size() < 2) {
            throw new IllegalArgumentException("AI enrichment payload is invalid");
        }

        return new ReadingDtos.AdminAiEnrichResponse(payload.draftExplanation(), payload.answerSourceHint(), payload.learnerTip(), payload.draftDistractors(), payload.shortSummary());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private String requestWithRetry(String systemPrompt, String userTemplate, Map<String, Object> params) {
        Exception last = null;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                log.debug("Reading AI call attempt {} with configured temperature {}", attempt, temperature);
                String content = chatClientBuilder.build().prompt()
                    .system(systemPrompt)
                    .user(u -> u.text(userTemplate).params(params))
                    .call()
                    .content();
                if (!isBlank(content)) {
                    return content;
                }
            } catch (Exception ex) {
                last = ex;
                log.warn("Reading AI call attempt {} failed", attempt, ex);
            }
        }
        throw new IllegalStateException("Reading AI call failed after retries", last);
    }

    private ExplainPayload parseExplainPayload(String content) {
        JsonNode root = parseJson(content);
        if (root == null) {
            return null;
        }
        return new ExplainPayload(
            readText(root, "correctAnswerExplanation"),
            readText(root, "wrongAnswerReason"),
            readText(root, "strategyTip")
        );
    }

    private EnrichPayload parseEnrichPayload(String content) {
        JsonNode root = parseJson(content);
        if (root == null) {
            return null;
        }
        return new EnrichPayload(
            readText(root, "draftExplanation"),
            readText(root, "answerSourceHint"),
            readText(root, "learnerTip"),
            readStringList(root.path("draftDistractors")),
            readText(root, "shortSummary")
        );
    }

    private JsonNode parseJson(String content) {
        if (isBlank(content)) {
            return null;
        }
        try {
            return objectMapper.readTree(stripMarkdownCodeFence(content));
        } catch (Exception _) {
            log.warn("Reading AI payload is not valid JSON");
            return null;
        }
    }

    private String readText(JsonNode root, String field) {
        JsonNode node = root.path(field);
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        String value = objectMapper.convertValue(node, String.class);
        return isBlank(value) ? null : value.trim();
    }

    private List<String> readStringList(JsonNode node) {
        if (node == null || !node.isArray()) {
            return List.of();
        }
        List<String> values = new ArrayList<>();
        for (JsonNode item : node) {
            String value = objectMapper.convertValue(item, String.class);
            if (!isBlank(value)) {
                values.add(value.trim());
            }
        }
        return values;
    }

    private String stripMarkdownCodeFence(String raw) {
        String value = raw == null ? "" : raw.trim();
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

    public record ExplainPayload(String correctAnswerExplanation, String wrongAnswerReason, String strategyTip) {}
    public record EnrichPayload(String draftExplanation, String answerSourceHint, String learnerTip, List<String> draftDistractors, String shortSummary) {}
}
