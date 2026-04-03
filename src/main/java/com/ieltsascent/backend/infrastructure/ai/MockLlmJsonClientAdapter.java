package com.ieltsascent.backend.infrastructure.ai;

import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.application.ai.LlmJsonResponse;
import java.time.Duration;

public class MockLlmJsonClientAdapter implements LlmJsonClientPort {
    private final AiInteractionLogger interactionLogger;

    public MockLlmJsonClientAdapter(AiInteractionLogger interactionLogger) {
        this.interactionLogger = interactionLogger;
    }

    @Override
    public LlmJsonResponse generateStrictJson(LlmJsonRequest request) {
        String json = """
            {
              "overallBand": 6.5,
              "criteria": {
                "taskResponse": 6.5,
                "coherenceCohesion": 6.0,
                "lexicalResource": 6.0,
                "grammarRangeAccuracy": 6.5
              },
              "summary": {
                "strengths": ["Clear structure", "Relevant examples"],
                "weaknesses": ["Limited lexical variety", "Some grammar slips"]
              },
              "mistakes": [
                {
                  "category": "grammar",
                  "excerpt": "The people is",
                  "fix": "The people are",
                  "explanation": "Subject-verb agreement"
                }
              ],
              "improvementTips": ["Use more topic-specific vocabulary", "Vary sentence openings"],
              "rewriteExercises": [
                {
                  "instruction": "Rewrite using a complex sentence",
                  "sentence": "Technology changes life."
                }
              ],
              "warnings": {
                "offTopic": false,
                "tooShort": false
              }
            }
            """;
        LlmJsonResponse response = new LlmJsonResponse(json, "mock", request.model(), Duration.ZERO.toMillis(), null);
        interactionLogger.log(request, "mock", request.model(), 0, null, json);
        return response;
    }
}
