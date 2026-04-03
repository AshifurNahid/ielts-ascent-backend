package com.ieltsascent.backend.application.ai;

public interface LlmJsonClientPort {
    LlmJsonResponse generateStrictJson(LlmJsonRequest request);
}
