package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.readingtest.ReadingQuestion;
import com.ieltsascent.backend.infrastructure.persistence.reading.ReadingQuestionRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingAiService {
    private static final String EXPLAIN_PROMPT = """
        You are an IELTS reading assistant.
        Given:
        - passage
        - question
        - correct answer
        - user answer
        - question type
        Generate:
        1. a concise explanation of the correct answer
        2. why the user answer is wrong
        3. one short strategy tip
        Return clean structured JSON only.
        """;

    private static final String ADMIN_PROMPT = """
        You are an IELTS reading content assistant.
        Given a passage and question, generate:
        1. a short explanation
        2. one answer source hint
        3. one learner-friendly tip
        4. three plausible distractor options
        5. one short passage summary
        Return clean structured JSON only.
        """;

    private final ChatClient.Builder chatClientBuilder;
    private final ReadingQuestionRepository questionRepository;

    public ReadingDtos.ReadingAiExplainResponse explain(ReadingDtos.ReadingAiExplainRequest request) {
        ReadingQuestion q = questionRepository.findById(request.questionId()).orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        ExplainPayload payload = chatClientBuilder.build().prompt()
            .system(EXPLAIN_PROMPT)
            .user(u -> u.text("""
                Passage: {passage}
                Question: {question}
                Correct answer: {correctAnswer}
                User answer: {userAnswer}
                Question type: {questionType}
                """).params(Map.of(
                "passage", q.getPassage().getContent(),
                "question", q.getPrompt(),
                "correctAnswer", q.getCorrectAnswer(),
                "userAnswer", request.userAnswer() == null ? "(empty)" : request.userAnswer(),
                "questionType", q.getType().name()
            )))
            .call()
            .entity(ExplainPayload.class);

        if (payload == null || isBlank(payload.correctAnswerExplanation()) || isBlank(payload.wrongAnswerReason()) || isBlank(payload.strategyTip())) {
            throw new IllegalArgumentException("AI explanation payload is invalid");
        }

        return new ReadingDtos.ReadingAiExplainResponse(payload.correctAnswerExplanation(), payload.wrongAnswerReason(), payload.strategyTip());
    }

    public ReadingDtos.AdminAiEnrichResponse enrich(ReadingDtos.AdminAiEnrichRequest request) {
        EnrichPayload payload = chatClientBuilder.build().prompt()
            .system(ADMIN_PROMPT)
            .user(u -> u.text("""
                Passage: {passage}
                Question: {question}
                Correct answer: {correctAnswer}
                Question type: {questionType}
                """).params(Map.of(
                "passage", request.passageContent(),
                "question", request.questionPrompt(),
                "correctAnswer", request.correctAnswer(),
                "questionType", request.questionType().name()
            )))
            .call()
            .entity(EnrichPayload.class);

        if (payload == null || isBlank(payload.draftExplanation()) || isBlank(payload.answerSourceHint()) || isBlank(payload.learnerTip())
            || isBlank(payload.shortSummary()) || payload.draftDistractors() == null || payload.draftDistractors().size() < 2) {
            throw new IllegalArgumentException("AI enrichment payload is invalid");
        }

        return new ReadingDtos.AdminAiEnrichResponse(payload.draftExplanation(), payload.answerSourceHint(), payload.learnerTip(), payload.draftDistractors(), payload.shortSummary());
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public record ExplainPayload(String correctAnswerExplanation, String wrongAnswerReason, String strategyTip) {}
    public record EnrichPayload(String draftExplanation, String answerSourceHint, String learnerTip, List<String> draftDistractors, String shortSummary) {}
}
