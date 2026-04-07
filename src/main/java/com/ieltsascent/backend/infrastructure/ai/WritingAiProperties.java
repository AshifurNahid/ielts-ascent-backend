package com.ieltsascent.backend.infrastructure.ai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "ai")
public class WritingAiProperties {
    private String provider;
    private String baseUrl;
    private String apiKey;
    private Model model = new Model();
    private int timeoutMs = 20000;
    private int maxRetries = 2;
    private double temperature = 0.1;
    private Provider gemini = new Provider();
    private Provider groq = new Provider();
    private Provider openai = new Provider();

    @Setter
    @Getter
    public static class Model {
        private String writing;

    }

    @Setter
    @Getter
    public static class Provider {
        private String baseUrl;
        private String apiKey;

    }
}
