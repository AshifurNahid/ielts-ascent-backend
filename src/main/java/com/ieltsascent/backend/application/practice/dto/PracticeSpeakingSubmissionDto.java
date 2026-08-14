package com.ieltsascent.backend.application.practice.dto;

import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PracticeSpeakingSubmissionDto {
    private final Long recordingId;
    private final Double overallBand;
    private final String feedbackSummary;

    public static PracticeSpeakingSubmissionDto from(SpeakingRecordingMetadata recording) {
        return new PracticeSpeakingSubmissionDto(
            recording.getId(),
            recording.getEvaluationResult().getOverallBand(),
            recording.getEvaluationResult().getFeedbackSummary()
        );
    }
}

