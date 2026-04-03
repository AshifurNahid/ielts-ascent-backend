package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrammarAiService {
    private static final String LESSON_PROMPT = """
        You are an IELTS grammar assistant.
        Given a grammar topic, lesson context, learner level, and IELTS band context, generate:
        1. one simple explanation
        2. two correct example sentences
        3. one incorrect example sentence with correction
        4. one IELTS-style example
        5. one short learning hint
        Return clean structured JSON.
        """;

    private static final String FEEDBACK_PROMPT = """
        You are an IELTS grammar feedback assistant.
        Explain the user's grammar mistake clearly and provide one practical hint.
        Return JSON with fields: feedback, hint.
        """;

    private final ChatClient.Builder chatClientBuilder;

    public GrammarDtos.AiLessonEnrichmentResponse generateLessonDraft(GrammarDtos.AiLessonEnrichmentRequest request) {
        AiLessonPayload payload = chatClientBuilder.build().prompt()
            .system(LESSON_PROMPT)
            .user(user -> user.text("""
                Topic: {topic}
                Lesson title: {lessonTitle}
                Lesson content: {lessonContent}
                English level: {englishLevel}
                Current IELTS band: {currentBand}
                Target IELTS band: {targetBand}
                """).params(java.util.Map.of(
                "topic", request.topicTitle(),
                "lessonTitle", request.lessonTitle(),
                "lessonContent", request.lessonContent(),
                "englishLevel", request.englishLevel(),
                "currentBand", request.currentBand() == null ? "unknown" : request.currentBand(),
                "targetBand", request.targetBand() == null ? "unknown" : request.targetBand()
            )))
            .call()
            .entity(AiLessonPayload.class);
        validate(payload);
        return new GrammarDtos.AiLessonEnrichmentResponse(payload.simpleExplanation(), payload.correctExamples(),
            payload.incorrectExample(), payload.correction(), payload.ieltsStyleExample(), payload.learningHint());
    }

    public GrammarDtos.AiQuestionFeedbackResponse buildWrongAnswerFeedback(GrammarDtos.AiQuestionFeedbackRequest request) {
        AiFeedbackPayload payload = chatClientBuilder.build().prompt()
            .system(FEEDBACK_PROMPT)
            .user(user -> user.text("""
                Topic: {topic}
                Question type: {type}
                Question: {question}
                Correct answer: {correctAnswer}
                User answer: {userAnswer}
                User level: {level}
                """).params(java.util.Map.of(
                "topic", request.topicTitle(),
                "type", request.questionType(),
                "question", request.question(),
                "correctAnswer", request.correctAnswer(),
                "userAnswer", request.userAnswer() == null ? "(empty)" : request.userAnswer(),
                "level", request.englishLevel()
            )))
            .call()
            .entity(AiFeedbackPayload.class);
        if (payload == null || isBlank(payload.feedback()) || isBlank(payload.hint())) {
            throw new IllegalArgumentException("AI feedback payload is invalid");
        }
        return new GrammarDtos.AiQuestionFeedbackResponse(payload.feedback(), payload.hint());
    }

    private void validate(AiLessonPayload payload) {
        if (payload == null || isBlank(payload.simpleExplanation()) || isBlank(payload.incorrectExample()) ||
            isBlank(payload.correction()) || isBlank(payload.ieltsStyleExample()) || isBlank(payload.learningHint()) ||
            payload.correctExamples() == null || payload.correctExamples().size() != 2) {
            throw new IllegalArgumentException("AI enrichment payload is invalid");
        }
    }

    private boolean isBlank(String text) { return text == null || text.isBlank(); }

    public record AiLessonPayload(String simpleExplanation, List<String> correctExamples, String incorrectExample,
                                  String correction, String ieltsStyleExample, String learningHint) {}

    public record AiFeedbackPayload(String feedback, String hint) {}
}
