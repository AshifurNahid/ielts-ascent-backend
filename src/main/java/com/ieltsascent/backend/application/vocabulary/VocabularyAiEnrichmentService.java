package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExampleType;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyWordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyAiEnrichmentService {
    private static final String SYSTEM_PROMPT = """
        You are an IELTS vocabulary assistant.
        Given the word, level, and IELTS band context, generate:
        1. one simple explanation
        2. two IELTS writing examples
        3. one IELTS speaking example
        4. one simple sentence
        5. two useful collocations
        6. one common incorrect usage with explanation
        Return clean structured JSON only.
        """;

    private final ChatClient.Builder chatClientBuilder;
    private final VocabularyWordRepository vocabularyWordRepository;

    @Transactional
    public VocabularyDtos.AiEnrichmentResponse enrich(VocabularyDtos.AiEnrichmentRequest request) {
        VocabularyWord word = vocabularyWordRepository.findById(request.vocabularyWordId())
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));

        ChatClient chatClient = chatClientBuilder.build();
        AiVocabularyPayload payload = chatClient.prompt()
            .system(SYSTEM_PROMPT)
            .user(userSpec -> userSpec.text(buildPromptTemplate()).params(promptParams(word)))
            .call()
            .entity(AiVocabularyPayload.class);

        validate(payload);

        if (Boolean.TRUE.equals(request.includeImprovedMeaning())) {
            word.setSimpleMeaning(payload.simpleExplanation().trim());
        }
        mergeCollocations(word, payload.collocations());
        addDraftExamples(word, payload);
        word.setCommonMistake(payload.commonMistake().trim() + " " + payload.commonMistakeExplanation().trim());
        vocabularyWordRepository.save(word);

        return new VocabularyDtos.AiEnrichmentResponse(
            payload.simpleExplanation(),
            payload.writingExamples(),
            payload.speakingExample(),
            payload.simpleExample(),
            payload.collocations(),
            payload.commonMistake(),
            payload.commonMistakeExplanation()
        );
    }

    private String buildPromptTemplate() {
        return """
            Word: {word}
            Level: {level}
            IELTS band min: {minBand}
            IELTS band max: {maxBand}
            Existing simple meaning: {simpleMeaning}
            """;
    }

    private java.util.Map<String, Object> promptParams(VocabularyWord word) {
        return java.util.Map.of(
            "word", word.getWord(),
            "level", word.getLevel(),
            "minBand", word.getIeltsBandMin(),
            "maxBand", word.getIeltsBandMax(),
            "simpleMeaning", word.getSimpleMeaning()
        );
    }

    private void validate(AiVocabularyPayload payload) {
        if (payload == null) {
            throw new IllegalArgumentException("AI enrichment payload is empty");
        }
        if (isBlank(payload.simpleExplanation())
            || isBlank(payload.speakingExample())
            || isBlank(payload.simpleExample())
            || isBlank(payload.commonMistake())
            || isBlank(payload.commonMistakeExplanation())) {
            throw new IllegalArgumentException("AI payload misses required fields");
        }
        if (payload.writingExamples() == null || payload.writingExamples().size() != 2) {
            throw new IllegalArgumentException("AI output must contain exactly 2 IELTS writing examples");
        }
        if (payload.collocations() == null || payload.collocations().size() != 2) {
            throw new IllegalArgumentException("AI output must contain exactly 2 collocations");
        }
    }

    private void mergeCollocations(VocabularyWord word, List<String> collocations) {
        word.getCollocations().addAll(
            collocations.stream()
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(String::toLowerCase)
                .toList()
        );
    }

    private void addDraftExamples(VocabularyWord word, AiVocabularyPayload payload) {
        addExample(word, payload.simpleExample(), VocabularyExampleType.SIMPLE);
        payload.writingExamples().forEach(example -> addExample(word, example, VocabularyExampleType.IELTS_WRITING));
        addExample(word, payload.speakingExample(), VocabularyExampleType.IELTS_SPEAKING);
    }

    private void addExample(VocabularyWord word, String sentence, VocabularyExampleType type) {
        VocabularyExample example = new VocabularyExample();
        example.setType(type);
        example.setSentence(sentence.trim());
        example.setAiGenerated(true);
        example.setApproved(false);
        word.addExample(example);
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    public record AiVocabularyPayload(
        String simpleExplanation,
        List<String> writingExamples,
        String speakingExample,
        String simpleExample,
        List<String> collocations,
        String commonMistake,
        String commonMistakeExplanation
    ) {
    }
}
