package com.ieltsascent.backend.application.writing;

import tools.jackson.databind.ObjectMapper;
import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import com.ieltsascent.backend.application.ai.LlmJsonRequest;
import com.ieltsascent.backend.application.ai.LlmJsonResponse;
import com.ieltsascent.backend.application.ai.PromptTemplateService;
import com.ieltsascent.backend.domain.content.WritingPrompt;
import com.ieltsascent.backend.infrastructure.ai.WritingAiProperties;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WritingAiServiceTest {

    @Test
    void repairsInvalidJsonOnce() {
        WritingAiProperties properties = new WritingAiProperties();
        WritingAiProperties.Model model = new WritingAiProperties.Model();
        model.setWriting("test-model");
        properties.setModel(model);
        properties.setTemperature(0.1);

        LlmJsonClientPort client = new LlmJsonClientPort() {
            private int callCount = 0;

            @Override
            public LlmJsonResponse generateStrictJson(LlmJsonRequest request) {
                callCount++;
                if (callCount == 1) {
                    return new LlmJsonResponse("{\"overallBand\": 6}", "mock", "test-model", 10, null);
                }
                String valid = """
                    {
                      "overallBand": 6.5,
                      "criteria": {
                        "taskResponse": 6.0,
                        "coherenceCohesion": 6.5,
                        "lexicalResource": 6.0,
                        "grammarRangeAccuracy": 6.5
                      },
                      "summary": {"strengths": ["a"], "weaknesses": ["b"]},
                      "mistakes": [],
                      "improvementTips": [],
                      "rewriteExercises": [],
                      "warnings": {"offTopic": false, "tooShort": false}
                    }
                    """;
                return new LlmJsonResponse(valid, "mock", "test-model", 10, null);
            }
        };

        WritingAiService service = new WritingAiService(
            client,
            new PromptTemplateService(),
            properties,
            new ObjectMapper()
        );

        WritingPrompt prompt = new WritingPrompt();
        prompt.setTaskType("TASK_2");
        WritingAiResult result = service.evaluateWriting(UUID.randomUUID(), prompt, "Sample essay", 7.0);

        assertThat(result.overallBand()).isEqualTo(6.5);
        assertThat(result.taskResponseBand()).isEqualTo(6.0);
    }
}
