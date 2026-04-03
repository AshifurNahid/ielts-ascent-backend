package com.ieltsascent.backend.infrastructure.ai;

import java.time.Duration;
import java.util.function.Predicate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LlmWebClientSupport {
    private final WebClient webClient;
    private final int timeoutMs;
    private final int maxRetries;

    public LlmWebClientSupport(WebClient webClient, int timeoutMs, int maxRetries) {
        this.webClient = webClient;
        this.timeoutMs = timeoutMs;
        this.maxRetries = maxRetries;
    }

    public <T> T post(String uri, Object body, Class<T> responseType, String correlationId) {
        return webClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .header("X-Correlation-Id", correlationId)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .timeout(Duration.ofMillis(timeoutMs))
            .retryWhen(Retry.backoff(maxRetries, Duration.ofMillis(200))
                .filter(retryable()))
            .block();
    }

    private Predicate<Throwable> retryable() {
        return throwable -> {
            if (throwable instanceof WebClientResponseException ex) {
                return ex.getStatusCode().value() == 429 || ex.getStatusCode().is5xxServerError();
            }
            return false;
        };
    }
}
