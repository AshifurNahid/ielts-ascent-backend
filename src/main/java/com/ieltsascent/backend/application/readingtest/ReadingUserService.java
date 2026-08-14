package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.readingtest.*;
import com.ieltsascent.backend.domain.readingtest.enums.*;
import com.ieltsascent.backend.domain.vocabulary.EnglishLevel;
import com.ieltsascent.backend.infrastructure.persistence.reading.*;
import jakarta.validation.ValidationException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingUserService {
    private final ReadingPassageRepository passageRepository;
    private final ReadingQuestionRepository questionRepository;
    private final ReadingTestRepository testRepository;
    private final ReadingTestPassageRepository testPassageRepository;
    private final UserReadingProfileRepository profileRepository;
    private final ReadingAttemptRepository attemptRepository;
    private final ReadingQuestionAttemptRepository questionAttemptRepository;
    private final ReadingSkillProgressService skillProgressService;
    private final ReadingRecommendationService recommendationService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingTestResponse> availableTests(Long userId) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        return testRepository.findAvailable(profile.getPremiumUser()).stream().map(ReadingMapper::toTestResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingTestResponse> availableTests() {
        return availableTests(currentUserProvider.userId());
    }


    public ReadingDtos.ReadingProfileResponse profile(Long userId) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        return new ReadingDtos.ReadingProfileResponse(
            profile.getEnglishLevel(),
            profile.getCurrentIeltsBand(),
            profile.getTargetIeltsBand(),
            profile.getPremiumUser()
        );
    }

    @Transactional(readOnly = true)
    public ReadingDtos.ReadingProfileResponse profile() {
        return profile(currentUserProvider.userId());
    }

    @Transactional(readOnly = true)
    public ReadingDtos.ReadingTestDetailResponse testDetail(Long userId, Long testId) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        ReadingTest test = testRepository.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test not found"));
        if (test.getStatus() != ReadingContentStatus.PUBLISHED || (!profile.getPremiumUser() && Boolean.TRUE.equals(test.getPremium()))) {
            throw new ResourceNotFoundException("Test not available");
        }
        List<ReadingDtos.TestPassagePayload> payload = testPassageRepository.findByReadingTestIdOrderByOrderIndexAsc(testId).stream().map(tp -> {
            ReadingPassage p = tp.getPassage();
            if (p.getStatus() != ReadingContentStatus.PUBLISHED || (!profile.getPremiumUser() && Boolean.TRUE.equals(p.getPremium()))) {
                return null;
            }
            List<ReadingQuestion> questions = questionRepository.findByPassageIdAndStatusOrderByOrderIndexAsc(p.getId(), ReadingContentStatus.PUBLISHED);
            List<ReadingDtos.ReadingQuestionResponse> visibleQuestions = questions.stream()
                .filter(q -> profile.getPremiumUser() || !Boolean.TRUE.equals(q.getPremium()))
                .map(ReadingMapper::toUserQuestionResponse)
                .toList();
            if (visibleQuestions.isEmpty()) {
                return null;
            }
            return new ReadingDtos.TestPassagePayload(ReadingMapper.toPassageResponse(p), visibleQuestions);
        }).filter(Objects::nonNull).toList();
        return new ReadingDtos.ReadingTestDetailResponse(ReadingMapper.toTestResponse(test), payload);
    }

    @Transactional(readOnly = true)
    public ReadingDtos.ReadingTestDetailResponse testDetail(Long testId) {
        return testDetail(currentUserProvider.userId(), testId);
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingPassageResponse> passagesForPractice(Long userId) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        return passageRepository.findEligibleForUser(profile.getPremiumUser(), profile.getCurrentIeltsBand(), profile.getTargetIeltsBand())
            .stream().limit(20).map(ReadingMapper::toPassageResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingPassageResponse> passagesForPractice() {
        return passagesForPractice(currentUserProvider.userId());
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingQuestionResponse> practiceByType(Long userId, ReadingQuestionKind type) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        return questionRepository.findPracticeByType(type, profile.getPremiumUser()).stream().limit(30).map(ReadingMapper::toUserQuestionResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingQuestionResponse> practiceByType(ReadingQuestionKind type) {
        return practiceByType(currentUserProvider.userId(), type);
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingQuestionResponse> practiceByPassage(Long userId, Long passageId) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        return questionRepository.findPracticeByPassage(passageId, profile.getPremiumUser()).stream()
            .map(ReadingMapper::toUserQuestionResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.ReadingQuestionResponse> practiceByPassage(Long passageId) {
        return practiceByPassage(currentUserProvider.userId(), passageId);
    }

    @Transactional
    public ReadingDtos.AttemptSummaryResponse submitAttempt(Long userId, ReadingDtos.AttemptSubmitRequest request) {
        Instant startedAt = toInstant(request.startedAt());
        Instant completedAt = toInstant(request.completedAt());
        validateAttemptWindow(startedAt, completedAt);
        UserReadingProfile profile = getOrCreateProfile(userId);
        List<ReadingQuestion> questions = questionRepository.findAllById(request.answers().stream().map(ReadingDtos.AttemptQuestionSubmit::questionId).toList());
        Map<Long, ReadingQuestion> byId = questions.stream().collect(java.util.stream.Collectors.toMap(ReadingQuestion::getId, q -> q));
        if (byId.size() != request.answers().size()) {
            throw new ValidationException("Some submitted question IDs are invalid.");
        }
        for (ReadingQuestion question : questions) {
            if (question.getStatus() != ReadingContentStatus.PUBLISHED || question.getPassage().getStatus() != ReadingContentStatus.PUBLISHED) {
                throw new ValidationException("Submitted answers include unpublished reading content.");
            }
            if (!profile.getPremiumUser() && (Boolean.TRUE.equals(question.getPremium()) || Boolean.TRUE.equals(question.getPassage().getPremium()))) {
                throw new ValidationException("Submitted answers include premium-only reading content.");
            }
        }

        if (request.mode() == ReadingAttemptMode.FULL_TEST && request.readingTestId() == null) {
            throw new ValidationException("readingTestId is required for FULL_TEST mode.");
        }
        if (request.mode() == ReadingAttemptMode.PASSAGE_PRACTICE && request.passageId() == null) {
            throw new ValidationException("passageId is required for PASSAGE_PRACTICE mode.");
        }

        ReadingAttempt attempt = new ReadingAttempt();
        attempt.setUserId(userId);
        attempt.setReadingTestId(request.readingTestId());
        attempt.setPassageId(request.passageId());
        attempt.setMode(request.mode());
        attempt.setStartedAt(startedAt);
        attempt.setCompletedAt(completedAt);
        attempt.setTotalQuestions(request.answers().size());

        int correctAnswers = 0;
        int totalTime = 0;
        for (ReadingDtos.AttemptQuestionSubmit answer : request.answers()) {
            ReadingQuestion question = byId.get(answer.questionId());
            if (normalize(question.getCorrectAnswer()).equals(normalize(answer.userAnswer()))) correctAnswers++;
            totalTime += answer.timeSpentSeconds();
        }
        attempt.setCorrectAnswers(correctAnswers);
        attempt.setTotalTimeSeconds(totalTime);
        attempt.setScorePercent(request.answers().isEmpty() ? 0 : correctAnswers * 100.0 / request.answers().size());
        ReadingAttempt savedAttempt = attemptRepository.save(attempt);

        List<ReadingSkillProgressService.ReadingSkillUpdateEvent> events = new ArrayList<>();
        for (ReadingDtos.AttemptQuestionSubmit answer : request.answers()) {
            ReadingQuestion question = byId.get(answer.questionId());
            boolean correct = normalize(question.getCorrectAnswer()).equals(normalize(answer.userAnswer()));

            ReadingQuestionAttempt qa = new ReadingQuestionAttempt();
            qa.setReadingAttempt(savedAttempt);
            qa.setReadingQuestionId(question.getId());
            qa.setUserId(userId);
            qa.setUserAnswer(answer.userAnswer());
            qa.setCorrect(correct);
            qa.setTimeSpentSeconds(answer.timeSpentSeconds());
            questionAttemptRepository.save(qa);

            events.add(new ReadingSkillProgressService.ReadingSkillUpdateEvent(ReadingSkillType.QUESTION_TYPE, question.getType().name(), correct, answer.timeSpentSeconds(), 75));
            events.add(new ReadingSkillProgressService.ReadingSkillUpdateEvent(ReadingSkillType.TOPIC, question.getPassage().getTopicTag().toUpperCase(Locale.ROOT), correct, answer.timeSpentSeconds(), 75));
            events.add(new ReadingSkillProgressService.ReadingSkillUpdateEvent(ReadingSkillType.DIFFICULTY, question.getDifficulty().name() + "_PASSAGES", correct, answer.timeSpentSeconds(), 85));
            events.add(new ReadingSkillProgressService.ReadingSkillUpdateEvent(ReadingSkillType.TIME_MANAGEMENT, "TIME_MANAGEMENT", correct, answer.timeSpentSeconds(), 70));
        }
        List<UserReadingSkillProgress> updated = skillProgressService.updateAfterAttempt(userId, events);
        recommendationService.recommend(profile);

        return new ReadingDtos.AttemptSummaryResponse(savedAttempt.getId(), savedAttempt.getTotalQuestions(), savedAttempt.getCorrectAnswers(),
            savedAttempt.getScorePercent(), savedAttempt.getTotalTimeSeconds(), updated.stream().map(ReadingMapper::toSkillResponse).toList());
    }

    @Transactional
    public ReadingDtos.AttemptSummaryResponse submitAttempt(ReadingDtos.AttemptSubmitRequest request) {
        return submitAttempt(currentUserProvider.userId(), request);
    }

    @Transactional(readOnly = true)
    public ReadingDtos.AttemptResultResponse result(Long userId, Long attemptId) {
        ReadingAttempt attempt = attemptRepository.findByIdAndUserId(attemptId, userId).orElseThrow(() -> new ResourceNotFoundException("Attempt not found"));
        Map<Long, ReadingQuestion> questionMap = questionRepository.findAllById(
            questionAttemptRepository.findByReadingAttemptIdOrderByCreatedAtAsc(attemptId).stream().map(ReadingQuestionAttempt::getReadingQuestionId).toList()
        ).stream().collect(java.util.stream.Collectors.toMap(ReadingQuestion::getId, q -> q));

        List<ReadingDtos.AttemptQuestionResult> results = questionAttemptRepository.findByReadingAttemptIdOrderByCreatedAtAsc(attemptId).stream().map(qa -> {
            ReadingQuestion q = questionMap.get(qa.getReadingQuestionId());
            return new ReadingDtos.AttemptQuestionResult(qa.getReadingQuestionId(), qa.getUserAnswer(), qa.getCorrect(), q == null ? null : q.getCorrectAnswer(),
                q == null ? null : q.getExplanation(), qa.getTimeSpentSeconds());
        }).toList();

        return new ReadingDtos.AttemptResultResponse(attempt.getId(), attempt.getTotalQuestions(), attempt.getCorrectAnswers(), attempt.getScorePercent(),
            attempt.getTotalTimeSeconds(), attempt.getMode(), attempt.getStartedAt(), attempt.getCompletedAt(), results);
    }

    @Transactional(readOnly = true)
    public ReadingDtos.AttemptResultResponse result(Long attemptId) {
        return result(currentUserProvider.userId(), attemptId);
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.SkillProgressResponse> progress(Long userId) {
        return skillProgressService.getProgress(userId).stream().map(ReadingMapper::toSkillResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.SkillProgressResponse> progress() {
        return progress(currentUserProvider.userId());
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.SkillProgressResponse> weakAreas(Long userId) {
        return skillProgressService.getProgress(userId).stream().filter(s -> s.getStatus() == ReadingSkillStatus.WEAK || s.getStatus() == ReadingSkillStatus.IMPROVING)
            .map(ReadingMapper::toSkillResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ReadingDtos.SkillProgressResponse> weakAreas() {
        return weakAreas(currentUserProvider.userId());
    }

    @Transactional
    public ReadingDtos.RecommendationResponse recommendation(Long userId) {
        return recommendationService.recommend(getOrCreateProfile(userId));
    }

    @Transactional
    public ReadingDtos.RecommendationResponse recommendation() {
        return recommendation(currentUserProvider.userId());
    }

    @Transactional
    public void upsertProfile(Long userId, ReadingDtos.ReadingProfileUpsertRequest request) {
        UserReadingProfile profile = getOrCreateProfile(userId);
        profile.setEnglishLevel(request.englishLevel());
        profile.setCurrentIeltsBand(request.currentIeltsBand());
        profile.setTargetIeltsBand(request.targetIeltsBand());
        profile.setPremiumUser(request.premiumUser());
        profileRepository.save(profile);
    }

    @Transactional
    public void upsertProfile(ReadingDtos.ReadingProfileUpsertRequest request) {
        upsertProfile(currentUserProvider.userId(), request);
    }

    private UserReadingProfile getOrCreateProfile(Long userId) {
        return profileRepository.findByUserId(userId).orElseGet(() -> {
            UserReadingProfile p = new UserReadingProfile();
            p.setUserId(userId);
            p.setEnglishLevel(EnglishLevel.INTERMEDIATE);
            p.setCurrentIeltsBand(5.5);
            p.setTargetIeltsBand(6.5);
            p.setPremiumUser(false);
            return profileRepository.save(p);
        });
    }

    private void validateAttemptWindow(java.time.Instant startedAt, java.time.Instant completedAt) {
        if (completedAt.isBefore(startedAt)) {
            throw new ValidationException("completedAt must be after startedAt");
        }
        long hours = Duration.between(startedAt, completedAt).toHours();
        if (hours > 4) {
            throw new ValidationException("Attempt duration is unrealistic. Please resubmit.");
        }
    }

    private Instant toInstant(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    private static String normalize(String v) { return v == null ? "" : v.trim().toLowerCase(Locale.ROOT); }
}
