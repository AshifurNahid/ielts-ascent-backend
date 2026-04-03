package com.ieltsascent.backend.infrastructure.ai;

import com.ieltsascent.backend.application.ai.LlmJsonClientPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WritingAiProperties.class)
public class WritingAiConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "ai", name = "provider", havingValue = "mock", matchIfMissing = true)
    public LlmJsonClientPort mockLlmJsonClientPort(AiInteractionLogger interactionLogger) {
        return new MockLlmJsonClientAdapter(interactionLogger);
    }

    @Bean
    @ConditionalOnProperty(prefix = "ai", name = "provider", havingValue = "gemini")
    public LlmJsonClientPort geminiLlmJsonClientPort(
        WritingAiProperties properties,
        AiInteractionLogger interactionLogger
    ) {
        return new GeminiLlmJsonClientAdapter(
            properties.getGemini(),
            properties.getTimeoutMs(),
            properties.getMaxRetries(),
            interactionLogger
        );
    }

    @Bean
    @ConditionalOnProperty(prefix = "ai", name = "provider", havingValue = "groq")
    public LlmJsonClientPort groqLlmJsonClientPort(
        WritingAiProperties properties,
        AiInteractionLogger interactionLogger
    ) {
        return new GroqLlmJsonClientAdapter(
            properties.getGroq(),
            properties.getTimeoutMs(),
            properties.getMaxRetries(),
            interactionLogger
        );
    }

    @Bean
    @ConditionalOnProperty(prefix = "ai", name = "provider", havingValue = "openai")
    public LlmJsonClientPort openaiLlmJsonClientPort(
        WritingAiProperties properties,
        AiInteractionLogger interactionLogger
    ) {
        return new OpenAiLlmJsonClientAdapter(
            properties.getOpenai(),
            properties.getTimeoutMs(),
            properties.getMaxRetries(),
            interactionLogger
        );
    }
}
