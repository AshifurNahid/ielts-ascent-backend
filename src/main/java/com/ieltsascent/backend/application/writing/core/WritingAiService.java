package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.domain.writing.WritingTaskType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import tools.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class WritingAiService {
    private final ChatClient.Builder chatClientBuilder;
    private final ObjectMapper objectMapper;

    public AiEvaluationResponse evaluateEssay(WritingTaskType taskType, String promptText, String essayText, String userHistorySummary) {
        String input = """
            You are an IELTS Writing evaluator.
            Evaluate the essay using IELTS rubric.
            Return ONLY strict JSON with fields:
            overallBand, taskResponseBand, coherenceBand, lexicalBand, grammarBand, confidence,
            strengths (string[]), weakPoints ([{category, weakKey, severity, explanation, suggestion}]),
            suggestions ([{suggestionType, originalText, suggestedText, explanation}]), summary.

            Context:
            taskType: %s
            prompt: %s
            essay: %s
            userHistorySummary: %s
            """.formatted(taskType, promptText, essayText, userHistorySummary == null ? "" : userHistorySummary);
        try {
            String content = chatClientBuilder.build().prompt().user(input).call().content();
            return objectMapper.readValue(content, AiEvaluationResponse.class);
        } catch (Exception ex) {
            log.warn("Writing AI evaluation failed", ex);
            return AiEvaluationResponse.failed();
        }
    }

    public String improveSection(String promptText, String essayText, String sectionText, String instruction) {
        String payload = """
            Improve one section of an IELTS essay.
            Return only the improved section and 1-2 sentence explanation.
            Prompt: %s
            Essay: %s
            Section: %s
            Instruction: %s
            """.formatted(promptText, essayText, sectionText, instruction);
        return chatClientBuilder.build().prompt().user(payload).call().content();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AiWeakPoint(
        String category,
        String weakKey,
        String severity,
        String explanation,
        String suggestion
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AiSuggestion(
        String suggestionType,
        String originalText,
        String suggestedText,
        String explanation
    ) {
    }
}
