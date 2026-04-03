package com.ieltsascent.backend.infrastructure.ai;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(int timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Provider getGemini() {
        return gemini;
    }

    public void setGemini(Provider gemini) {
        this.gemini = gemini;
    }

    public Provider getGroq() {
        return groq;
    }

    public void setGroq(Provider groq) {
        this.groq = groq;
    }

    public Provider getOpenai() {
        return openai;
    }

    public void setOpenai(Provider openai) {
        this.openai = openai;
    }

    public static class Model {
        private String writing;

        public String getWriting() {
            return writing;
        }

        public void setWriting(String writing) {
            this.writing = writing;
        }
    }

    public static class Provider {
        private String baseUrl;
        private String apiKey;

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
}
