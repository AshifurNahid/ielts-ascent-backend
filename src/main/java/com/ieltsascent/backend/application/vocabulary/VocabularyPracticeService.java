package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.vocabulary.UserVocabularyProgress;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import com.ieltsascent.backend.domain.vocabulary.VocabularyPracticeAttempt;
import com.ieltsascent.backend.domain.vocabulary.VocabularyPracticeQuestionType;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.UserVocabularyProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyPracticeAttemptRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyWordRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyPracticeService {
    private final UserVocabularyProgressRepository userVocabularyProgressRepository;
    private final VocabularyPracticeAttemptRepository practiceAttemptRepository;
    private final VocabularyWordRepository vocabularyWordRepository;

    @Transactional(readOnly = true)
    public VocabularyDtos.PracticeSessionResponse buildPracticeSession(List<VocabularyWord> words) {
        List<VocabularyDtos.PracticeQuestionItem> items = words.stream()
            .map(this::toQuestion)
            .toList();
        return new VocabularyDtos.PracticeSessionResponse(items);
    }

    @Transactional
    public VocabularyDtos.PracticeSubmitResponse submit(UUID userId, VocabularyDtos.SubmitPracticeResultRequest request) {
        int correct = 0;
        int wrong = 0;

        for (VocabularyDtos.PracticeAnswerRequest answer : request.answers()) {
            VocabularyWord word = vocabularyWordRepository.findById(answer.vocabularyWordId())
                .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));

            VocabularyPracticeAttempt attempt = new VocabularyPracticeAttempt();
            attempt.setUserId(userId);
            attempt.setVocabularyWord(word);
            attempt.setQuestionType(answer.questionType());
            attempt.setUserAnswer(answer.userAnswer());
            attempt.setCorrect(answer.correct());
            practiceAttemptRepository.save(attempt);

            upsertProgress(userId, word, answer.correct());
            if (Boolean.TRUE.equals(answer.correct())) {
                correct++;
            } else {
                wrong++;
            }
        }

        return new VocabularyDtos.PracticeSubmitResponse(request.answers().size(), correct, wrong);
    }

    @Transactional
    public void markDifficult(UUID userId, UUID wordId, boolean difficult) {
        VocabularyWord word = vocabularyWordRepository.findById(wordId)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));
        UserVocabularyProgress progress = userVocabularyProgressRepository.findByUserIdAndVocabularyWordId(userId, wordId)
            .orElseGet(() -> createProgress(userId, word));
        progress.setMarkedDifficult(difficult);
        userVocabularyProgressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public VocabularyDtos.ProgressSummaryResponse getSummary(UUID userId) {
        long mastered = userVocabularyProgressRepository.countByUserIdAndMasteryLevelGreaterThanEqual(userId, 0.85);
        long difficult = userVocabularyProgressRepository.countByUserIdAndMarkedDifficultTrue(userId);
        long practiced7days = userVocabularyProgressRepository.countByUserIdAndLastPracticedAtAfter(userId, Instant.now().minus(7, ChronoUnit.DAYS));
        long tracked = userVocabularyProgressRepository.findByUserId(userId).size();
        return new VocabularyDtos.ProgressSummaryResponse(mastered, difficult, practiced7days, tracked);
    }

    private VocabularyDtos.PracticeQuestionItem toQuestion(VocabularyWord word) {
        VocabularyPracticeQuestionType type = VocabularyPracticeQuestionType.values()[Math.abs(word.getWord().hashCode()) % VocabularyPracticeQuestionType.values().length];
        String prompt;
        List<String> options = new ArrayList<>();

        switch (type) {
            case MEANING_RECOGNITION -> {
                prompt = "Choose the best meaning for: " + word.getWord();
                options.add(word.getSimpleMeaning());
                options.add("A grammar tense marker");
                options.add("A pronunciation symbol");
                options.add("A punctuation rule");
            }
            case SYNONYM_MATCH -> {
                prompt = "Pick the closest synonym for: " + word.getWord();
                String synonym = word.getSynonyms().stream().findFirst().orElse(word.getSimpleMeaning());
                options.add(synonym);
                options.add("irrelevant");
                options.add("mispronounce");
                options.add("disconnect");
            }
            case FILL_IN_THE_BLANK -> {
                prompt = fillBlankPrompt(word).orElse("Complete the sentence using: " + word.getWord());
                options.add(word.getWord());
                options.add("however");
                options.add("therefore");
                options.add("although");
            }
            case USAGE_SELECTION -> {
                prompt = "Which sentence uses the word correctly?";
                options.addAll(usageOptions(word));
            }
            default -> throw new IllegalStateException("Unexpected practice question type");
        }

        Collections.shuffle(options, new Random(word.getWord().hashCode()));
        return new VocabularyDtos.PracticeQuestionItem(word.getId(), word.getWord(), type, prompt, options);
    }

    private Optional<String> fillBlankPrompt(VocabularyWord word) {
        return word.getExamples().stream()
            .filter(example -> Boolean.TRUE.equals(example.getApproved()))
            .map(VocabularyExample::getSentence)
            .findFirst()
            .map(sentence -> sentence.replace(word.getWord(), "_____"));
    }

    private List<String> usageOptions(VocabularyWord word) {
        String correctUsage = word.getExamples().stream()
            .filter(example -> Boolean.TRUE.equals(example.getApproved()))
            .map(VocabularyExample::getSentence)
            .findFirst()
            .orElse("Students should use '" + word.getWord() + "' in academic writing when relevant.");

        return List.of(
            correctUsage,
            "I am very " + word.getWord() + "ly yesterday in school.",
            "The " + word.getWord() + " are quickly because grammar.",
            "Do not " + word.getWord() + " if vocabulary is noun only."
        );
    }

    private void upsertProgress(UUID userId, VocabularyWord word, boolean correct) {
        UserVocabularyProgress progress = userVocabularyProgressRepository.findByUserIdAndVocabularyWordId(userId, word.getId())
            .orElseGet(() -> createProgress(userId, word));
        progress.setExposureCount(progress.getExposureCount() + 1);
        if (correct) {
            progress.setCorrectCount(progress.getCorrectCount() + 1);
        } else {
            progress.setWrongCount(progress.getWrongCount() + 1);
            progress.setMarkedDifficult(true);
        }
        int total = Math.max(1, progress.getCorrectCount() + progress.getWrongCount());
        progress.setMasteryLevel((double) progress.getCorrectCount() / total);
        progress.setLastPracticedAt(Instant.now());
        progress.setNextRecommendedAt(correct ? Instant.now().plus(2, ChronoUnit.DAYS) : Instant.now().plus(6, ChronoUnit.HOURS));
        userVocabularyProgressRepository.save(progress);
    }

    private UserVocabularyProgress createProgress(UUID userId, VocabularyWord word) {
        UserVocabularyProgress progress = new UserVocabularyProgress();
        progress.setUserId(userId);
        progress.setVocabularyWord(word);
        return progress;
    }
}
