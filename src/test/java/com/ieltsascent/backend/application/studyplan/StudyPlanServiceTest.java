package com.ieltsascent.backend.application.studyplan;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.application.ai.CurrentStateSnapshot;
import com.ieltsascent.backend.application.ai.CurrentStateSnapshotService;
import com.ieltsascent.backend.application.assessment.AssessmentService;
import com.ieltsascent.backend.application.auth.ProfileReadiness;
import com.ieltsascent.backend.application.auth.UserService;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import com.ieltsascent.backend.infrastructure.persistence.StudyPlanRepository;
import com.ieltsascent.backend.infrastructure.persistence.StudyTaskRepository;
import com.ieltsascent.backend.infrastructure.persistence.TaskCompletionRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudyPlanServiceTest {
    private UserRepository userRepository;
    private AiRecommendationEngine recommendationEngine;
    private CurrentStateSnapshotService snapshotService;
    private UserService userService;
    private StudyPlanService service;

    @BeforeEach
    void setUp() {
        StudyPlanRepository studyPlanRepository = mock(StudyPlanRepository.class);
        StudyTaskRepository studyTaskRepository = mock(StudyTaskRepository.class);
        userRepository = mock(UserRepository.class);
        recommendationEngine = mock(AiRecommendationEngine.class);
        TaskCompletionRepository taskCompletionRepository = mock(TaskCompletionRepository.class);
        AssessmentService assessmentService = mock(AssessmentService.class);
        snapshotService = mock(CurrentStateSnapshotService.class);
        userService = mock(UserService.class);
        CurrentUserProvider currentUserProvider = mock(CurrentUserProvider.class);

        service = new StudyPlanService(
            studyPlanRepository,
            studyTaskRepository,
            userRepository,
            recommendationEngine,
            taskCompletionRepository,
            assessmentService,
            snapshotService,
            userService,
            currentUserProvider
        );
    }

    @Test
    void generateCrashPlanReturnsTopThreeTasks() {
        Long userId = 1L;
        User user = new User();
        CurrentStateSnapshot snapshot = mock(CurrentStateSnapshot.class);

        when(userService.resolveReadiness(userId)).thenReturn(ProfileReadiness.ready(true, true));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(snapshotService.buildSnapshot(userId)).thenReturn(snapshot);

        List<StudyTask> tasks = List.of(task("T1"), task("T2"), task("T3"), task("T4"));
        when(recommendationEngine.recommendNextBestActions(user, snapshot)).thenReturn(tasks);

        List<StudyTask> crashPlan = service.generateCrashPlan(userId);

        assertThat(crashPlan).extracting(StudyTask::getTitle).containsExactly("T1", "T2", "T3");
    }

    private StudyTask task(String title) {
        StudyTask task = new StudyTask();
        task.setTitle(title);
        task.setTaskType("READING");
        task.setEstimatedMinutes(15);
        return task;
    }
}
