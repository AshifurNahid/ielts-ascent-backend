package com.ieltsascent.backend.application.studyplan;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.application.ai.CurrentStateSnapshot;
import com.ieltsascent.backend.application.ai.CurrentStateSnapshotService;
import com.ieltsascent.backend.application.assessment.AssessmentService;
import com.ieltsascent.backend.application.auth.ProfileReadiness;
import com.ieltsascent.backend.application.auth.UserService;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.studyplan.StudyPlan;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import com.ieltsascent.backend.domain.studyplan.TaskCompletion;
import com.ieltsascent.backend.infrastructure.persistence.StudyPlanRepository;
import com.ieltsascent.backend.infrastructure.persistence.StudyTaskRepository;
import com.ieltsascent.backend.infrastructure.persistence.TaskCompletionRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyPlanService {
    private final StudyPlanRepository studyPlanRepository;
    private final StudyTaskRepository studyTaskRepository;
    private final UserRepository userRepository;
    private final AiRecommendationEngine recommendationEngine;
    private final TaskCompletionRepository taskCompletionRepository;
    private final AssessmentService assessmentService;
    private final CurrentStateSnapshotService snapshotService;
    private final UserService userService;

    public StudyPlan generate(UUID userId, DiagnosticResult diagnosticResult) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        StudyPlan plan = recommendationEngine.generateInitialPlan(user, diagnosticResult);
        return studyPlanRepository.save(plan);
    }

    public StudyPlan generateFromDiagnostic(UUID userId, UUID diagnosticSessionId) {
        DiagnosticResult diagnosticResult = assessmentService.getResult(diagnosticSessionId);
        return generate(userId, diagnosticResult);
    }

    public StudyPlan getCurrent(UUID userId) {
        return studyPlanRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Study plan not found"));
    }

    public List<StudyTask> getTodayTasks(UUID userId) {
        ensureSuggestionReadiness(userId);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CurrentStateSnapshot snapshot = snapshotService.buildSnapshot(userId);
        return recommendationEngine.recommendNextBestActions(user, snapshot);
    }

    public List<StudyTask> generateCrashPlan(UUID userId) {
        return getTodayTasks(userId).stream().limit(3).toList();
    }

    public TaskCompletion completeTask(UUID userId, UUID taskId) {
        TaskCompletion completion = new TaskCompletion();
        completion.setCompletedAt(Instant.now());
        completion.setStudyTask(studyTaskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found")));
        completion.setUser(userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        return taskCompletionRepository.save(completion);
    }

    private void ensureSuggestionReadiness(UUID userId) {
        ProfileReadiness readiness = userService.resolveReadiness(userId);
        if (!readiness.readyForPersonalizedSuggestions()) {
            throw new IllegalStateException(readiness.guidanceMessage());
        }
    }
}
