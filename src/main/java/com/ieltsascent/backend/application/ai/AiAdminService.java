package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.studyplan.StudyTask;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiAdminService {
    private final AiSpeakingEvaluationClient aiSpeakingEvaluationClient;
    private final AiRecommendationEngine aiRecommendationEngine;
    private final UserRepository userRepository;
    private final CurrentStateSnapshotService snapshotService;

    public SpeakingEvaluationResult evaluateSpeaking(String audioUrl, Integer durationSeconds) {
        SpeakingRecordingMetadata metadata = buildMetadata(audioUrl, durationSeconds);
        return aiSpeakingEvaluationClient.evaluateSpeaking(metadata);
    }

    public StoryOutline storyBuilder(String topic) {
        return aiSpeakingEvaluationClient.generateSpeakingStoryOutline(topic);
    }

    public ShadowingComparisonResult analyzeShadowing(String audioUrl, Integer durationSeconds, String referenceSampleId) {
        SpeakingRecordingMetadata metadata = buildMetadata(audioUrl, durationSeconds);
        return aiSpeakingEvaluationClient.analyzeShadowing(metadata, referenceSampleId);
    }

    public List<StudyTask> nextActions(UUID userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return aiRecommendationEngine.recommendNextBestActions(user, snapshotService.buildSnapshot(userId));
    }

    public ExamPrediction examPrediction(UUID userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return aiRecommendationEngine.predictExamOutcome(user);
    }

    private SpeakingRecordingMetadata buildMetadata(String audioUrl, Integer durationSeconds) {
        SpeakingRecordingMetadata metadata = new SpeakingRecordingMetadata();
        metadata.setAudioUrl(audioUrl);
        metadata.setDurationSeconds(durationSeconds);
        return metadata;
    }
}
