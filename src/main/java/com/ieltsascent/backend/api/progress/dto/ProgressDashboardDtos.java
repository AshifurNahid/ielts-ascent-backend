package com.ieltsascent.backend.api.progress.dto;

import com.ieltsascent.backend.application.progress.ProgressEnums.ConfidenceLevel;
import com.ieltsascent.backend.application.progress.ProgressEnums.ModuleType;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import java.time.Instant;
import java.util.List;

public final class ProgressDashboardDtos {
    private ProgressDashboardDtos() {}

    public record DashboardResponse(
        Overview overview,
        List<SkillProgress> skills,
        WeeklyActivity weeklyActivity,
        Prediction prediction,
        List<WeakArea> weakAreas
    ) {}

    public record Overview(
        Double currentOverallBand,
        Double targetBand,
        Double progressPercent,
        Double bandDeltaLast30Days,
        ConfidenceLevel confidenceLevel,
        Instant lastUpdated
    ) {}

    public record SkillProgress(
        SkillType skillType,
        Double currentBand,
        Double targetBand,
        Double progressPercent,
        Double trendValue,
        ConfidenceLevel confidenceLevel,
        Instant lastActivityAt
    ) {}

    public record WeeklyActivity(
        int totalMinutes,
        List<DailyActivity> days
    ) {}

    public record DailyActivity(
        String day,
        int minutes
    ) {}

    public record Prediction(
        Double predictedOverallBand,
        Double predictedListeningBand,
        Double predictedReadingBand,
        Double predictedWritingBand,
        Double predictedSpeakingBand,
        ConfidenceLevel confidenceLevel,
        String summary,
        String nextStep,
        List<String> keyFactors
    ) {}

    public record WeakArea(
        ModuleType moduleType,
        String areaType,
        String areaKey,
        String status
    ) {}
}
