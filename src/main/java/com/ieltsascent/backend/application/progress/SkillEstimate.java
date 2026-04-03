package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.application.progress.ProgressEnums.ConfidenceLevel;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import java.time.Instant;
import java.util.List;

public record SkillEstimate(
    SkillType skillType,
    Double currentBand,
    Double trendValue,
    ConfidenceLevel confidenceLevel,
    Instant lastActivityAt,
    int recentCount,
    boolean stale,
    List<String> factors
) {}
