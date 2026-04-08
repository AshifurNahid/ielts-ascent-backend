package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.DashboardResponse;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Overview;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.Prediction;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.SkillProgress;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeakArea;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeeklyActivity;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressDashboardService {
    private final UserRepository userRepository;
    private final SkillEstimateService skillEstimateService;
    private final PredictionService predictionService;
    private final WeeklyActivityAggregationService weeklyActivityAggregationService;
    private final WeakAreaAggregationService weakAreaAggregationService;
    private final ProgressNarrativeAiService progressNarrativeAiService;
    private final DiagnosticResultRepository diagnosticResultRepository;

    public DashboardResponse getDashboard(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Double targetBand = user.getProfile() != null ? user.getProfile().getTargetBand() : null;

        Map<SkillType, SkillEstimate> estimates = skillEstimateService.estimateForUser(userId);
        Prediction prediction = predictionService.compute(userId, estimates);
        List<WeakArea> weakAreas = weakAreaAggregationService.aggregate(userId);
        var narrative = progressNarrativeAiService.buildNarrative(prediction, estimates, weakAreas);
        Prediction enrichedPrediction = new Prediction(
            prediction.predictedOverallBand(),
            prediction.predictedListeningBand(),
            prediction.predictedReadingBand(),
            prediction.predictedWritingBand(),
            prediction.predictedSpeakingBand(),
            prediction.confidenceLevel(),
            narrative.summary(),
            narrative.nextStep(),
            prediction.keyFactors()
        );

        List<SkillProgress> skills = estimates.values().stream()
            .map(estimate -> new SkillProgress(
                estimate.skillType(),
                estimate.currentBand(),
                targetBand,
                ProgressMath.progressPercent(estimate.currentBand(), targetBand),
                estimate.trendValue(),
                estimate.confidenceLevel(),
                estimate.lastActivityAt()
            ))
            .sorted(Comparator.comparing(SkillProgress::skillType))
            .toList();

        var overallAverage = estimates.values().stream()
            .map(SkillEstimate::currentBand)
            .filter(value -> value != null && value > 0)
            .mapToDouble(Double::doubleValue)
            .average();
        Double overallCurrentBand = overallAverage.isPresent() ? ProgressMath.roundBand(overallAverage.getAsDouble()) : null;
        Double deltaLast30Days = calculateBandDelta(userId, overallCurrentBand);
        WeeklyActivity weeklyActivity = weeklyActivityAggregationService.aggregate(userId);

        Overview overview = new Overview(
            overallCurrentBand,
            targetBand,
            ProgressMath.progressPercent(overallCurrentBand, targetBand),
            deltaLast30Days,
            enrichedPrediction.confidenceLevel(),
            Instant.now()
        );

        return new DashboardResponse(overview, skills, weeklyActivity, enrichedPrediction, weakAreas);
    }

    public Prediction getPrediction(Long userId) {
        Map<SkillType, SkillEstimate> estimates = skillEstimateService.estimateForUser(userId);
        Prediction prediction = predictionService.compute(userId, estimates);
        List<WeakArea> weakAreas = weakAreaAggregationService.aggregate(userId);
        var narrative = progressNarrativeAiService.buildNarrative(prediction, estimates, weakAreas);
        return new Prediction(
            prediction.predictedOverallBand(),
            prediction.predictedListeningBand(),
            prediction.predictedReadingBand(),
            prediction.predictedWritingBand(),
            prediction.predictedSpeakingBand(),
            prediction.confidenceLevel(),
            narrative.summary(),
            narrative.nextStep(),
            prediction.keyFactors()
        );
    }

    public List<SkillProgress> getSkills(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Double targetBand = user.getProfile() != null ? user.getProfile().getTargetBand() : null;
        return skillEstimateService.estimateForUser(userId).values().stream()
            .map(estimate -> new SkillProgress(
                estimate.skillType(),
                estimate.currentBand(),
                targetBand,
                ProgressMath.progressPercent(estimate.currentBand(), targetBand),
                estimate.trendValue(),
                estimate.confidenceLevel(),
                estimate.lastActivityAt()
            ))
            .sorted(Comparator.comparing(SkillProgress::skillType))
            .toList();
    }

    public WeeklyActivity getWeeklyActivity(Long userId) {
        return weeklyActivityAggregationService.aggregate(userId);
    }

    public List<WeakArea> getWeakAreas(Long userId) {
        return weakAreaAggregationService.aggregate(userId);
    }

    private Double calculateBandDelta(Long userId, Double currentOverallBand) {
        if (currentOverallBand == null) {
            return null;
        }
        Instant cutoff = Instant.now().minus(30, ChronoUnit.DAYS);
        return diagnosticResultRepository.findFirstBySessionUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(userId, cutoff)
            .map(result -> result.getOverallBand() == null ? null : ProgressMath.round1(currentOverallBand - result.getOverallBand()))
            .orElse(null);
    }
}
