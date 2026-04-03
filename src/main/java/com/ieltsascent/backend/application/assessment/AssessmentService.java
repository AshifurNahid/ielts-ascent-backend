package com.ieltsascent.backend.application.assessment;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.assessment.DiagnosticSession;
import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticSessionRepository;
import com.ieltsascent.backend.infrastructure.persistence.MicroSkillScoreRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssessmentService {
    private final DiagnosticSessionRepository sessionRepository;
    private final DiagnosticResultRepository resultRepository;
    private final MicroSkillScoreRepository microSkillScoreRepository;
    private final UserRepository userRepository;
    private final AiRecommendationEngine recommendationEngine;

    public DiagnosticSession startDiagnostic(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        DiagnosticSession session = new DiagnosticSession();
        session.setUser(user);
        session.setStartedAt(Instant.now());
        return sessionRepository.save(session);
    }

    public DiagnosticResult completeDiagnostic(UUID sessionId) {
        DiagnosticSession session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setCompletedAt(Instant.now());
        sessionRepository.save(session);

        DiagnosticResult result = new DiagnosticResult();
        result.setSession(session);
        result.setOverallBand(recommendationEngine.predictExamOutcome(session.getUser()).overallBand());
        result.setSummaryId(UUID.randomUUID());
        return resultRepository.save(result);
    }

    public DiagnosticResult getResult(UUID sessionId) {
        return resultRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }

    public List<MicroSkillScore> listScores(UUID sessionId) {
        DiagnosticResult result = resultRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
        return microSkillScoreRepository.findByDiagnosticResultId(result.getId());
    }
}
