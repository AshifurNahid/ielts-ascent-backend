package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.readingtest.*;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingContentStatus;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingDifficulty;
import com.ieltsascent.backend.domain.readingtest.enums.ReadingQuestionKind;
import com.ieltsascent.backend.infrastructure.persistence.reading.*;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingAdminService {
    private final ReadingPassageRepository passageRepository;
    private final ReadingQuestionRepository questionRepository;
    private final ReadingTestRepository testRepository;
    private final ReadingTestPassageRepository testPassageRepository;

    @Transactional
    public ReadingPassage createPassage(ReadingDtos.PassageUpsertRequest request) {
        validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        ReadingPassage passage = new ReadingPassage();
        apply(passage, request);
        return passageRepository.save(passage);
    }

    @Transactional
    public ReadingPassage updatePassage(Long id, ReadingDtos.PassageUpsertRequest request) {
        validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        ReadingPassage passage = passageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passage not found"));
        apply(passage, request);
        return passageRepository.save(passage);
    }

    @Transactional
    public ReadingPassage updatePassageStatus(Long id, ReadingContentStatus status) {
        ReadingPassage passage = passageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passage not found"));
        passage.setStatus(status);
        return passageRepository.save(passage);
    }

    @Transactional(readOnly = true)
    public Page<ReadingPassage> listPassages(ReadingContentStatus status, ReadingDifficulty difficulty, Boolean premium, String topicTag, String query, Pageable pageable) {
        return passageRepository.searchAdmin(status, difficulty, premium, normalize(topicTag), normalize(query), pageable);
    }

    @Transactional
    public ReadingQuestion createQuestion(ReadingDtos.QuestionUpsertRequest request) {
        ReadingQuestion question = new ReadingQuestion();
        apply(question, request);
        return questionRepository.save(question);
    }

    @Transactional
    public ReadingQuestion updateQuestion(Long id, ReadingDtos.QuestionUpsertRequest request) {
        ReadingQuestion question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        apply(question, request);
        return questionRepository.save(question);
    }

    @Transactional
    public ReadingQuestion updateQuestionStatus(Long id, ReadingContentStatus status) {
        ReadingQuestion question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        question.setStatus(status);
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Page<ReadingQuestion> listQuestions(ReadingContentStatus status, ReadingQuestionKind type, Long passageId, Boolean premium, String query, Pageable pageable) {
        return questionRepository.searchAdmin(status, type, passageId, premium, normalize(query), pageable);
    }

    @Transactional
    public ReadingTest createTest(ReadingDtos.TestUpsertRequest request) {
        ReadingTest test = new ReadingTest();
        apply(test, request);
        return testRepository.save(test);
    }

    @Transactional
    public ReadingTest updateTest(Long id, ReadingDtos.TestUpsertRequest request) {
        ReadingTest test = testRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        apply(test, request);
        return testRepository.save(test);
    }

    @Transactional
    public ReadingTest updateTestStatus(Long id, ReadingContentStatus status) {
        ReadingTest test = testRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        test.setStatus(status);
        return testRepository.save(test);
    }

    @Transactional(readOnly = true)
    public Page<ReadingTest> listTests(ReadingContentStatus status, ReadingDifficulty difficulty, Boolean premium, String query, Pageable pageable) {
        return testRepository.searchAdmin(status, difficulty, premium, normalize(query), pageable);
    }

    @Transactional
    public void assignPassages(Long testId, ReadingDtos.AssignPassagesRequest request) {
        ReadingTest test = testRepository.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        testPassageRepository.deleteByReadingTestId(testId);
        List<ReadingTestPassage> entries = request.passages().stream().map(p -> {
            ReadingPassage passage = passageRepository.findById(p.passageId()).orElseThrow(() -> new ResourceNotFoundException("Passage not found"));
            ReadingTestPassage mapping = new ReadingTestPassage();
            mapping.setReadingTest(test);
            mapping.setPassage(passage);
            mapping.setOrderIndex(p.orderIndex());
            return mapping;
        }).toList();
        testPassageRepository.saveAll(entries);
    }

    private void apply(ReadingPassage passage, ReadingDtos.PassageUpsertRequest request) {
        passage.setTitle(request.title().trim());
        passage.setContent(request.content().trim());
        passage.setShortDescription(request.shortDescription());
        passage.setTopicTag(request.topicTag().trim());
        passage.setDifficulty(request.difficulty());
        passage.setIeltsBandMin(request.ieltsBandMin());
        passage.setIeltsBandMax(request.ieltsBandMax());
        passage.setEstimatedReadingMinutes(request.estimatedReadingMinutes());
        passage.setPremium(request.premium());
        passage.setStatus(request.status());
    }

    private void apply(ReadingQuestion question, ReadingDtos.QuestionUpsertRequest request) {
        ReadingPassage passage = passageRepository.findById(request.passageId()).orElseThrow(() -> new ResourceNotFoundException("Passage not found"));
        question.setPassage(passage);
        question.setGroupNumber(request.groupNumber());
        question.setType(request.type());
        question.setPrompt(request.prompt().trim());
        question.setOptions(ReadingMapper.normalize(request.options()));
        question.setCorrectAnswer(request.correctAnswer().trim());
        question.setExplanation(request.explanation());
        question.setAnswerSourceHint(request.answerSourceHint());
        question.setDifficulty(request.difficulty());
        question.setOrderIndex(request.orderIndex());
        question.setPremium(request.premium());
        question.setStatus(request.status());
        question.setTags(ReadingMapper.normalize(request.tags()));
    }

    private void apply(ReadingTest test, ReadingDtos.TestUpsertRequest request) {
        test.setTitle(request.title().trim());
        test.setDescription(request.description());
        test.setTotalTimeMinutes(request.totalTimeMinutes());
        test.setDifficulty(request.difficulty());
        test.setPremium(request.premium());
        test.setStatus(request.status());
    }

    private static String normalize(String value) { return value == null || value.isBlank() ? null : value.trim().toLowerCase(); }
    private static void validateBand(Double min, Double max) { if (max < min) throw new ValidationException("IELTS band max must be >= min"); }
}
