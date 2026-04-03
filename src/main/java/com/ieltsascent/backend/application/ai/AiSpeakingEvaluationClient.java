package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;

public interface AiSpeakingEvaluationClient {
    SpeakingEvaluationResult evaluateSpeaking(SpeakingRecordingMetadata recordingMetadata);

    StoryOutline generateSpeakingStoryOutline(String topic);

    ShadowingComparisonResult analyzeShadowing(SpeakingRecordingMetadata userRecording, String referenceSampleId);
}
