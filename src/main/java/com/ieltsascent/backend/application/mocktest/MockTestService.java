package com.ieltsascent.backend.application.mocktest;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.mocktest.MockTestOverallResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import com.ieltsascent.backend.domain.mocktest.MockTestSession;
import com.ieltsascent.backend.infrastructure.persistence.MockTestSectionResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.MockTestSessionRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockTestService {
    private final MockTestSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final AiRecommendationEngine recommendationEngine;
    private final MockTestSectionResultRepository sectionResultRepository;
    private final CurrentUserProvider currentUserProvider;

    public MockTestSession startSession(String mode) {
        return startSession(currentUserProvider.userId(), mode);
    }

    public MockTestSession startSession(Long userId, String mode) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        MockTestSession session = new MockTestSession();
        session.setUser(user);
        session.setMode(mode);
        session.setStartedAt(Instant.now());
        return sessionRepository.save(session);
    }

    public MockTestOverallResult finalizeSession(Long sessionId) {
        MockTestSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setCompletedAt(Instant.now());
        sessionRepository.save(session);

        ExamPrediction prediction = recommendationEngine.predictExamOutcome(session.getUser());
        MockTestOverallResult result = new MockTestOverallResult();
        result.setSession(session);
        result.setOverallBand(prediction.overallBand());
        result.setSummary(prediction.summary());
        return result;
    }

    public MockTestSectionResult recordSection(Long sessionId, String section, Double bandScore, String feedback) {
        MockTestSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        MockTestSectionResult result = new MockTestSectionResult();
        result.setSession(session);
        result.setSection(section);
        result.setBandScore(bandScore);
        result.setFeedback(feedback);
        return sectionResultRepository.save(result);
    }
}
