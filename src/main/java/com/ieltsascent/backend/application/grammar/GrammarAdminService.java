package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.grammar.GrammarLesson;
import com.ieltsascent.backend.domain.grammar.GrammarLessonExample;
import com.ieltsascent.backend.domain.grammar.GrammarQuestion;
import com.ieltsascent.backend.domain.grammar.GrammarTopic;
import com.ieltsascent.backend.infrastructure.persistence.GrammarTopicRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarLessonExampleRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarLessonRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarQuestionRepository;
import jakarta.validation.ValidationException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrammarAdminService {
    private final GrammarTopicRepository topicRepository;
    private final GrammarLessonRepository lessonRepository;
    private final GrammarLessonExampleRepository exampleRepository;
    private final GrammarQuestionRepository questionRepository;

    @Transactional
    public GrammarTopic createTopic(GrammarDtos.TopicUpsertRequest request) { validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        GrammarTopic topic = new GrammarTopic();
        apply(topic, request);
        return topicRepository.save(topic);
    }

    @Transactional
    public GrammarTopic updateTopic(UUID id, GrammarDtos.TopicUpsertRequest request) { validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        GrammarTopic topic = getTopic(id); apply(topic, request); return topicRepository.save(topic);
    }

    @Transactional(readOnly = true)
    public Page<GrammarTopic> listTopics(com.ieltsascent.backend.domain.grammar.GrammarContentStatus status,
                                         com.ieltsascent.backend.domain.grammar.GrammarLevel level,
                                         Boolean premium, String query, Pageable pageable) {
        return topicRepository.searchAdmin(status, level, premium, normalize(query), pageable);
    }

    @Transactional
    public GrammarLesson createLesson(GrammarDtos.LessonUpsertRequest request) {
        GrammarLesson lesson = new GrammarLesson();
        apply(lesson, request);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public GrammarLesson updateLesson(UUID id, GrammarDtos.LessonUpsertRequest request) {
        GrammarLesson lesson = lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        apply(lesson, request);
        return lessonRepository.save(lesson);
    }

    @Transactional
    public GrammarLessonExample createLessonExample(GrammarDtos.LessonExampleUpsertRequest request) {
        GrammarLessonExample example = new GrammarLessonExample();
        apply(example, request);
        return exampleRepository.save(example);
    }

    @Transactional
    public GrammarLessonExample updateLessonExample(UUID id, GrammarDtos.LessonExampleUpsertRequest request) {
        GrammarLessonExample example = exampleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson example not found"));
        apply(example, request);
        return exampleRepository.save(example);
    }

    @Transactional
    public void deleteLessonExample(UUID id) {
        exampleRepository.delete(exampleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lesson example not found")));
    }

    @Transactional
    public GrammarQuestion createQuestion(GrammarDtos.QuestionUpsertRequest request) {
        validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        GrammarQuestion question = new GrammarQuestion();
        apply(question, request);
        return questionRepository.save(question);
    }

    @Transactional
    public GrammarQuestion updateQuestion(UUID id, GrammarDtos.QuestionUpsertRequest request) {
        validateBand(request.ieltsBandMin(), request.ieltsBandMax());
        GrammarQuestion question = questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
        apply(question, request);
        return questionRepository.save(question);
    }

    @Transactional(readOnly = true)
    public Page<GrammarQuestion> listQuestions(com.ieltsascent.backend.domain.grammar.GrammarContentStatus status,
                                               com.ieltsascent.backend.domain.grammar.GrammarQuestionType type,
                                               UUID topicId, Boolean premium, String query, Pageable pageable) {
        return questionRepository.searchAdmin(status, type, topicId, premium, normalize(query), pageable);
    }

    private void apply(GrammarTopic topic, GrammarDtos.TopicUpsertRequest request) {
        topic.setTitle(request.title().trim()); topic.setDescription(request.description()); topic.setIcon(request.icon());
        topic.setOrderIndex(request.orderIndex()); topic.setLevel(request.level()); topic.setIeltsBandMin(request.ieltsBandMin());
        topic.setIeltsBandMax(request.ieltsBandMax()); topic.setPremium(request.premium()); topic.setUnlockedByDefault(request.unlockedByDefault());
        topic.setStatus(request.status()); topic.setPrerequisiteTopicId(request.prerequisiteTopicId());
    }

    private void apply(GrammarLesson lesson, GrammarDtos.LessonUpsertRequest request) {
        GrammarTopic topic = getTopic(request.topicId());
        lesson.setTopic(topic); lesson.setTitle(request.title().trim()); lesson.setContent(request.content().trim());
        lesson.setOrderIndex(request.orderIndex()); lesson.setDifficulty(request.difficulty()); lesson.setPremium(request.premium());
        lesson.setStatus(request.status()); lesson.setCompletedByDefault(request.completedByDefault());
    }

    private void apply(GrammarLessonExample example, GrammarDtos.LessonExampleUpsertRequest request) {
        GrammarLesson lesson = lessonRepository.findById(request.lessonId()).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        example.setLesson(lesson); example.setCorrectSentence(request.correctSentence().trim()); example.setIncorrectSentence(request.incorrectSentence());
        example.setExplanation(request.explanation().trim()); example.setAiGenerated(request.aiGenerated()); example.setApproved(request.approved());
    }

    private void apply(GrammarQuestion question, GrammarDtos.QuestionUpsertRequest request) {
        GrammarTopic topic = getTopic(request.topicId());
        GrammarLesson lesson = request.lessonId() == null ? null : lessonRepository.findById(request.lessonId())
            .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        question.setTopic(topic); question.setLesson(lesson); question.setType(request.type()); question.setQuestion(request.question().trim());
        question.setOptions(GrammarMapper.normalizeStringSet(request.options())); question.setCorrectAnswer(request.correctAnswer().trim());
        question.setExplanation(request.explanation()); question.setDifficulty(request.difficulty()); question.setPremium(request.premium());
        question.setIeltsBandMin(request.ieltsBandMin()); question.setIeltsBandMax(request.ieltsBandMax());
        question.setTags(GrammarMapper.normalizeStringSet(request.tags())); question.setStatus(request.status());
        question.setAiGenerated(request.aiGenerated()); question.setReviewedByAdmin(request.reviewedByAdmin());
    }

    private GrammarTopic getTopic(UUID id) {
        return topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
    }

    private static String normalize(String query) { return query == null || query.isBlank() ? null : query.trim().toLowerCase(); }

    private static void validateBand(Double min, Double max) { if (max < min) throw new ValidationException("IELTS band max must be >= min"); }
}
