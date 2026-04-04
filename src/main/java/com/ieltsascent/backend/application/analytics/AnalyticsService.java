package com.ieltsascent.backend.application.analytics;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.application.ai.AiRecommendationEngine;
import com.ieltsascent.backend.application.ai.ExamPrediction;
import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.MicroSkillScoreRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingRecordingRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final AiRecommendationEngine recommendationEngine;
    private final UserRepository userRepository;
    private final DiagnosticResultRepository diagnosticResultRepository;
    private final MicroSkillScoreRepository microSkillScoreRepository;
    private final WritingSubmissionRepository writingSubmissionRepository;
    private final SpeakingRecordingRepository speakingRecordingRepository;

    public Map<String, Object> overview(UUID userId) {
        DiagnosticResult latestDiagnostic = diagnosticResultRepository.findFirstBySessionUserIdOrderByCreatedAtDesc(userId)
            .orElse(null);
        Double currentBand = latestDiagnostic != null
            ? latestDiagnostic.getOverallBand()
            : resolveLatestSubmissionBand(userId);
        if (currentBand == null) {
            currentBand = userRepository.findById(userId)
                .map(user -> user.getProfile() != null ? user.getProfile().getCurrentBand() : null)
                .orElse(null);
        }

        Map<String, Double> skills = buildSkillOverview(latestDiagnostic, userId);
        Map<String, Object> overview = new LinkedHashMap<>();
        overview.put("currentBand", currentBand);
        overview.put("skills", skills);
        return overview;
    }

    public Map<String, Object> trends(UUID userId) {
        var recentResults = diagnosticResultRepository.findTop7BySessionUserIdOrderByCreatedAtDesc(userId);
        var recentBands = recentResults.stream()
            .sorted(Comparator.comparing(DiagnosticResult::getCreatedAt))
            .map(DiagnosticResult::getOverallBand)
            .filter(Objects::nonNull)
            .toList();

        Map<String, java.util.List<Integer>> microSkills = new LinkedHashMap<>();
        recentResults.stream()
            .sorted(Comparator.comparing(DiagnosticResult::getCreatedAt))
            .forEach(result -> {
                var scores = microSkillScoreRepository.findByDiagnosticResultId(result.getId());
                scores.forEach(score -> microSkills.computeIfAbsent(score.getSkillName(), key -> new java.util.ArrayList<>())
                    .add(score.getScore()));
            });

        return Map.of(
            "weeklyBands", recentBands,
            "microSkills", microSkills
        );
    }

    public Map<String, Object> weaknesses(UUID userId) {
        DiagnosticResult latestDiagnostic = diagnosticResultRepository.findFirstBySessionUserIdOrderByCreatedAtDesc(userId)
            .orElse(null);
        if (latestDiagnostic == null) {
            return Map.of("weakAreas", java.util.List.of());
        }
        var scores = microSkillScoreRepository.findByDiagnosticResultId(latestDiagnostic.getId());
        var weakAreas = scores.stream()
            .filter(score -> score.getScore() != null && score.getScore() < 60)
            .sorted(Comparator.comparing(MicroSkillScore::getScore))
            .map(MicroSkillScore::getSkillName)
            .toList();
        return Map.of("weakAreas", weakAreas);
    }

    public ExamPrediction examPrediction(UUID userId) {
        var user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return recommendationEngine.predictExamOutcome(user);
    }

    private Double resolveLatestSubmissionBand(UUID userId) {
        Double writingBand = writingSubmissionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .map(WritingSubmission::getOverallBand)
            .orElse(null);
        Double speakingBand = speakingRecordingRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .map(SpeakingRecordingMetadata::getEvaluationResult)
            .map(result -> result.getOverallBand())
            .orElse(null);
        if (writingBand == null && speakingBand == null) {
            return null;
        }
        if (writingBand == null) {
            return speakingBand;
        }
        if (speakingBand == null) {
            return writingBand;
        }
        return (writingBand + speakingBand) / 2.0;
    }

    private Map<String, Double> buildSkillOverview(DiagnosticResult latestDiagnostic, UUID userId) {
        Map<String, Double> skillBands = new LinkedHashMap<>();
        if (latestDiagnostic != null) {
            var scores = microSkillScoreRepository.findByDiagnosticResultId(latestDiagnostic.getId());
            var grouped = scores.stream()
                .collect(Collectors.groupingBy(score -> score.getSkillName().split("_")[0]));
            grouped.forEach((skill, skillScores) -> {
                double avg = skillScores.stream()
                    .mapToInt(MicroSkillScore::getScore)
                    .average()
                    .orElse(0);
                skillBands.put(skill, Math.round(avg / 10.0) / 10.0);
            });
        }

        writingSubmissionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .map(WritingSubmission::getOverallBand)
            .ifPresent(band -> skillBands.put("writing", band));
        speakingRecordingRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            .map(SpeakingRecordingMetadata::getEvaluationResult)
            .map(result -> result.getOverallBand())
            .ifPresent(band -> skillBands.put("speaking", band));
        return skillBands;
    }
}
