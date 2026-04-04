package com.ieltsascent.backend.infrastructure.ai;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.application.ai.AiSpeakingEvaluationClient;
import com.ieltsascent.backend.application.ai.AiTextAnalysisClient;
import com.ieltsascent.backend.application.ai.CurrentStateSnapshot;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.application.ai.ShadowingComparisonResult;
import com.ieltsascent.backend.application.ai.StoryOutline;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.studyplan.StudyPlan;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalAiProviderClient implements AiTextAnalysisClient, AiSpeakingEvaluationClient, AiRecommendationEngine {
    private final RestClient restClient;

    public ExternalAiProviderClient(
        @Value("${ai.base-url}") String baseUrl,
        @Value("${ai.api-key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();
    }

    @Override
    public String generateTemplateForUserStyle(User user, List<WritingSubmission> history) {
        WritingTemplateResponse response = restClient.post()
            .uri("/writing/template")
            .body(new WritingTemplateRequest(user.getId(), user.getFullName(), history.size()))
            .retrieve()
            .body(WritingTemplateResponse.class);
        return Objects.requireNonNull(response, "AI template response missing").template();
    }

    @Override
    public SpeakingEvaluationResult evaluateSpeaking(SpeakingRecordingMetadata recordingMetadata) {
        SpeakingEvaluationResponse response = restClient.post()
            .uri("/speaking/evaluate")
            .body(new SpeakingEvaluationRequest(recordingMetadata.getAudioUrl(), recordingMetadata.getDurationSeconds()))
            .retrieve()
            .body(SpeakingEvaluationResponse.class);
        return toSpeakingEvaluationResult(response);
    }

    @Override
    public StoryOutline generateSpeakingStoryOutline(String topic) {
        StoryOutline response = restClient.post()
            .uri("/speaking/story-builder")
            .body(new StoryBuilderRequest(topic))
            .retrieve()
            .body(StoryOutline.class);
        return Objects.requireNonNull(response, "AI story outline missing");
    }

    @Override
    public ShadowingComparisonResult analyzeShadowing(SpeakingRecordingMetadata userRecording, String referenceSampleId) {
        ShadowingComparisonResult response = restClient.post()
            .uri("/speaking/shadowing/analysis")
            .body(new ShadowingRequest(
                userRecording.getAudioUrl(),
                userRecording.getDurationSeconds(),
                referenceSampleId
            ))
            .retrieve()
            .body(ShadowingComparisonResult.class);
        return Objects.requireNonNull(response, "AI shadowing response missing");
    }

    @Override
    public StudyPlan generateInitialPlan(User user, DiagnosticResult diagnosticResult) {
        StudyPlanResponse response = restClient.post()
            .uri("/study-plan/generate")
            .body(new StudyPlanRequest(user.getId(), diagnosticResult.getId()))
            .retrieve()
            .body(StudyPlanResponse.class);
        return toStudyPlan(user, response);
    }

    @Override
    public List<StudyTask> recommendNextBestActions(User user, CurrentStateSnapshot snapshot) {
        NextActionsResponse response = restClient.post()
            .uri("/recommendations/next-actions")
            .body(new NextActionsRequest(user.getId(), snapshot))
            .retrieve()
            .body(NextActionsResponse.class);
        if (response == null || response.tasks() == null) {
            throw new IllegalStateException("AI next actions response missing");
        }
        return response.tasks().stream().map(this::toStudyTask).toList();
    }

    @Override
    public ExamPrediction predictExamOutcome(User user) {
        ExamPrediction response = restClient.post()
            .uri("/exam-prediction")
            .body(new ExamPredictionRequest(user.getId()))
            .retrieve()
            .body(ExamPrediction.class);
        return Objects.requireNonNull(response, "AI exam prediction missing");
    }

    private SpeakingEvaluationResult toSpeakingEvaluationResult(SpeakingEvaluationResponse response) {
        if (response == null) {
            throw new IllegalStateException("AI speaking evaluation response missing");
        }
        SpeakingEvaluationResult result = new SpeakingEvaluationResult();
        result.setFluency(response.fluency());
        result.setPronunciation(response.pronunciation());
        result.setLexicalResource(response.lexicalResource());
        result.setGrammar(response.grammar());
        result.setOverallBand(response.overallBand());
        result.setFeedbackSummary(response.feedbackSummary());
        return result;
    }

    private StudyPlan toStudyPlan(User user, StudyPlanResponse response) {
        if (response == null) {
            throw new IllegalStateException("AI study plan response missing");
        }
        StudyPlan plan = new StudyPlan();
        plan.setUser(user);
        plan.setStatus(response.status());
        return plan;
    }

    private StudyTask toStudyTask(StudyTaskResponse response) {
        StudyTask task = new StudyTask();
        task.setTaskType(response.taskType());
        task.setTitle(response.title());
        task.setEstimatedMinutes(response.estimatedMinutes());
        return task;
    }


    private record WritingTemplateRequest(java.util.UUID userId, String fullName, int submissionCount) {
    }

    private record WritingTemplateResponse(String template) {
    }

    private record SpeakingEvaluationRequest(String audioUrl, Integer durationSeconds) {
    }

    private record SpeakingEvaluationResponse(
        Double fluency,
        Double pronunciation,
        Double lexicalResource,
        Double grammar,
        Double overallBand,
        String feedbackSummary
    ) {
    }

    private record StoryBuilderRequest(String topic) {
    }

    private record ShadowingRequest(String audioUrl, Integer durationSeconds, String referenceSampleId) {
    }

    private record StudyPlanRequest(java.util.UUID userId, java.util.UUID diagnosticResultId) {
    }

    private record StudyPlanResponse(String status) {
    }

    private record NextActionsRequest(java.util.UUID userId, CurrentStateSnapshot snapshot) {
    }

    private record NextActionsResponse(List<StudyTaskResponse> tasks) {
    }

    private record StudyTaskResponse(String taskType, String title, Integer estimatedMinutes) {
    }

    private record ExamPredictionRequest(java.util.UUID userId) {
    }
}
