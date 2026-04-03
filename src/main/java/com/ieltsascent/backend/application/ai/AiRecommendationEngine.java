package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.studyplan.StudyPlan;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import java.util.List;

public interface AiRecommendationEngine {
    StudyPlan generateInitialPlan(User user, DiagnosticResult diagnosticResult);

    List<StudyTask> recommendNextBestActions(User user, CurrentStateSnapshot snapshot);

    ExamPrediction predictExamOutcome(User user);
}
