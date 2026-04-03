package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressNarrativeAiService {
    private static final String PROMPT = """
        You are an IELTS progress assistant.
        Given deterministic numeric estimates and confidence, generate concise JSON:
        {
          "summary": "...",
          "nextStep": "..."
        }
        Keep each field under 28 words.
        Do not include markdown.
        """;

    private final ChatClient.Builder chatClientBuilder;

    public NarrativeResult buildNarrative(
        Prediction prediction,
        Map<SkillType, SkillEstimate> estimates,
        List<WeakArea> weakAreas
    ) {
        try {
            SkillEstimate strongest = estimates.values().stream()
                .filter(s -> s.currentBand() != null)
                .max(Comparator.comparing(SkillEstimate::currentBand))
                .orElse(null);
            SkillEstimate weakest = estimates.values().stream()
                .filter(s -> s.currentBand() != null)
                .min(Comparator.comparing(SkillEstimate::currentBand))
                .orElse(null);

            NarrativePayload payload = chatClientBuilder.build().prompt()
                .system(PROMPT)
                .user(u -> u.text("""
                    Predicted overall band: {overall}
                    Confidence: {confidence}
                    Strongest skill: {strongest}
                    Weakest skill: {weakest}
                    Weak areas: {weakAreas}
                    Key factors: {factors}
                    """).params(Map.of(
                    "overall", String.valueOf(prediction.predictedOverallBand()),
                    "confidence", prediction.confidenceLevel().name(),
                    "strongest", strongest == null ? "unknown" : strongest.skillType().name(),
                    "weakest", weakest == null ? "unknown" : weakest.skillType().name(),
                    "weakAreas", weakAreas.toString(),
                    "factors", prediction.keyFactors().toString()
                )))
                .call()
                .entity(NarrativePayload.class);

            if (payload == null || isBlank(payload.summary()) || isBlank(payload.nextStep())) {
                return fallback(prediction, strongest, weakest);
            }
            return new NarrativeResult(payload.summary(), payload.nextStep());
        } catch (Exception ignored) {
            return fallback(prediction, null, null);
        }
    }

    private NarrativeResult fallback(Prediction prediction, SkillEstimate strongest, SkillEstimate weakest) {
        String strongestText = strongest == null ? "your strongest available skill" : strongest.skillType().name().toLowerCase();
        String weakestText = weakest == null ? "the weakest covered skill" : weakest.skillType().name().toLowerCase();
        String summary = "Current projection is band %.1f with %s confidence; %s leads while %s needs more focus."
            .formatted(prediction.predictedOverallBand(), prediction.confidenceLevel().name().toLowerCase(), strongestText,
                weakestText);
        String nextStep = "Complete at least one fresh writing and speaking evaluation this week to increase prediction reliability.";
        return new NarrativeResult(summary, nextStep);
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    public record NarrativePayload(String summary, String nextStep) {}

    public record NarrativeResult(String summary, String nextStep) {}
}
