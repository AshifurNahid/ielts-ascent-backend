package com.ieltsascent.backend.application.grammar;

import com.ieltsascent.backend.api.grammar.dto.GrammarDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.grammar.GrammarContentStatus;
import com.ieltsascent.backend.domain.grammar.GrammarLesson;
import com.ieltsascent.backend.domain.grammar.GrammarPracticeAttempt;
import com.ieltsascent.backend.domain.grammar.GrammarQuestion;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import com.ieltsascent.backend.domain.grammar.GrammarTopic;
import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import com.ieltsascent.backend.domain.grammar.UserGrammarProgress;
import com.ieltsascent.backend.infrastructure.persistence.GrammarTopicRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarDrillTemplateRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarLessonExampleRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarLessonRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarPracticeAttemptRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.GrammarQuestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProfileRepository;
import com.ieltsascent.backend.infrastructure.persistence.grammar.UserGrammarProgressRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrammarUserService {
    private final GrammarTopicRepository topicRepository;
    private final GrammarLessonRepository lessonRepository;
    private final GrammarLessonExampleRepository lessonExampleRepository;
    private final GrammarQuestionRepository questionRepository;
    private final GrammarDrillTemplateRepository drillTemplateRepository;
    private final GrammarPracticeAttemptRepository attemptRepository;
    private final UserGrammarProfileRepository profileRepository;
    private final UserGrammarProgressRepository progressRepository;
    private final GrammarRecommendationService recommendationService;
    private final GrammarAiService grammarAiService;
    private final UserGrammarProfileService userGrammarProfileService;

    @Transactional(readOnly = true)
    public List<GrammarTopic> getTopics(Long userId) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        return topicRepository.findAllByStatusOrderByOrderIndexAsc(GrammarContentStatus.PUBLISHED).stream()
            .filter(t -> profile.getPremiumUser() || !t.getPremium())
            .toList();
    }

    @Transactional(readOnly = true)
    public GrammarTopic getTopic(Long userId, Long topicId) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        GrammarTopic topic = topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("Topic not found"));
        if (topic.getStatus() != GrammarContentStatus.PUBLISHED || (!profile.getPremiumUser() && topic.getPremium())) {
            throw new ResourceNotFoundException("Topic not accessible");
        }
        return topic;
    }

    @Transactional(readOnly = true)
    public List<GrammarDtos.LessonResponse> lessonsByTopic(Long userId, Long topicId) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        return lessonRepository.findByTopicIdAndStatusOrderByOrderIndexAsc(topicId, GrammarContentStatus.PUBLISHED).stream()
            .filter(lesson -> profile.getPremiumUser() || !lesson.getPremium())
            .map(lesson -> GrammarMapper.toLessonResponse(lesson, lessonExampleRepository.findByLessonIdOrderByCreatedAtAsc(lesson.getId())))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<GrammarQuestion> getTopicQuestions(Long userId, Long topicId) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        return questionRepository.findByTopicIdAndStatus(topicId, GrammarContentStatus.PUBLISHED).stream()
            .filter(q -> profile.getPremiumUser() || !q.getPremium())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<GrammarQuestion> getDrillQuestions(Long userId, GrammarQuestionType type, int limit) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        return questionRepository.findByTypeAndStatus(type, GrammarContentStatus.PUBLISHED).stream()
            .filter(q -> profile.getPremiumUser() || !q.getPremium())
            .limit(limit)
            .toList();
    }

    @Transactional(readOnly = true)
    public GrammarDtos.ProgressSummaryResponse progressSummary(Long userId) {
        return GrammarMapper.toProgressSummary(progressRepository.findByUserId(userId));
    }

    @Transactional
    public void completeLesson(Long userId, Long lessonId) {
        GrammarLesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        UserGrammarProgress progress = progressRepository.findByUserIdAndTopicId(userId, lesson.getTopic().getId())
            .orElseGet(() -> initProgress(userId, lesson.getTopic()));
        progress.setCompletedLessons(Math.min(progress.getTotalLessons(), progress.getCompletedLessons() + 1));
        progress.setCompleted(progress.getCompletedLessons() >= progress.getTotalLessons());
        progress.setLastPracticedAt(Instant.now());
        progressRepository.save(progress);
    }

    @Transactional
    public GrammarDtos.SubmitPracticeResponse submitPractice(Long userId, GrammarDtos.SubmitPracticeRequest request) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        int correct = 0;
        List<GrammarDtos.WrongAnswerFeedback> feedback = new ArrayList<>();
        Set<String> weakTopics = new java.util.HashSet<>(profile.getWeakTopics());
        Set<GrammarQuestionType> weakDrillTypes = new java.util.HashSet<>(profile.getWeakDrillTypes());

        for (GrammarDtos.PracticeAnswerItem answer : request.answers()) {
            GrammarQuestion question = questionRepository.findById(answer.questionId()).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
            boolean isCorrect = question.getCorrectAnswer().trim().equalsIgnoreCase(answer.userAnswer() == null ? "" : answer.userAnswer().trim());
            if (isCorrect) correct++;
            GrammarPracticeAttempt attempt = GrammarMapper.toAttempt(userId, question, answer.userAnswer(), isCorrect);
            attemptRepository.save(attempt);
            updateProgress(userId, question, isCorrect);
            if (!isCorrect) {
                weakTopics.add(question.getTopic().getTitle().trim().toLowerCase());
                weakDrillTypes.add(question.getType());
                var aiFeedback = grammarAiService.buildWrongAnswerFeedback(new GrammarDtos.AiQuestionFeedbackRequest(
                    question.getQuestion(), question.getCorrectAnswer(), answer.userAnswer(), question.getTopic().getTitle(), question.getType(), profile.getEnglishLevel()));
                feedback.add(new GrammarDtos.WrongAnswerFeedback(question.getId(), question.getExplanation(), aiFeedback.feedback()));
            }
        }

        profile.setWeakTopics(weakTopics);
        profile.setWeakDrillTypes(weakDrillTypes);
        profileRepository.save(profile);

        int total = request.answers().size();
        return new GrammarDtos.SubmitPracticeResponse(total, correct, total - correct, weakTopics, weakDrillTypes, feedback);
    }

    @Transactional(readOnly = true)
    public GrammarDtos.RecommendationResponse recommendations(Long userId, int limit) {
        UserGrammarProfile profile = userGrammarProfileService.getOrCreate(userId);
        return recommendationService.recommend(userId, profile, limit);
    }

    private UserGrammarProgress initProgress(Long userId, GrammarTopic topic) {
        UserGrammarProgress progress = new UserGrammarProgress();
        progress.setUserId(userId);
        progress.setTopic(topic);
        progress.setTotalLessons((int) lessonRepository.countByTopicIdAndStatus(topic.getId(), GrammarContentStatus.PUBLISHED));
        progress.setUnlocked(topic.getUnlockedByDefault());
        return progress;
    }

    private void updateProgress(Long userId, GrammarQuestion question, boolean correct) {
        UserGrammarProgress progress = progressRepository.findByUserIdAndTopicId(userId, question.getTopic().getId())
            .orElseGet(() -> initProgress(userId, question.getTopic()));
        double delta = correct ? 4.0 : -3.0;
        progress.setMasteryScore(Math.max(0.0, Math.min(100.0, progress.getMasteryScore() + delta)));
        progress.setLastPracticedAt(Instant.now());
        progressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public List<com.ieltsascent.backend.domain.grammar.GrammarDrillTemplate> activeDrills() {
        return drillTemplateRepository.findByActiveTrueOrderByCreatedAtAsc();
    }
}
