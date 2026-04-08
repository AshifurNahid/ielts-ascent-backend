package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.application.progress.ProgressEnums.ConfidenceLevel;
import com.ieltsascent.backend.application.progress.ProgressEnums.SkillType;
import com.ieltsascent.backend.domain.mocktest.MockTestSectionResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.readingtest.ReadingAttempt;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.MockTestSectionResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingRecordingRepository;
import com.ieltsascent.backend.infrastructure.persistence.reading.ReadingAttemptRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillEstimateService {
    private static final Duration STALE_AFTER = Duration.ofDays(21);

    private final ReadingAttemptRepository readingAttemptRepository;
    private final MockTestSectionResultRepository mockTestSectionResultRepository;
    private final WritingSubmissionRepository writingSubmissionRepository;
    private final SpeakingRecordingRepository speakingRecordingRepository;

    public Map<SkillType, SkillEstimate> estimateForUser(Long userId) {
        Map<SkillType, SkillEstimate> estimates = new EnumMap<>(SkillType.class);
        estimates.put(SkillType.READING, estimateReading(userId));
        estimates.put(SkillType.LISTENING, estimateListening(userId));
        estimates.put(SkillType.WRITING, estimateWriting(userId));
        estimates.put(SkillType.SPEAKING, estimateSpeaking(userId));
        return estimates;
    }

    private SkillEstimate estimateReading(Long userId) {
        List<ReadingAttempt> attempts = readingAttemptRepository.findTop20ByUserIdOrderByCreatedAtDesc(userId);
        if (attempts.isEmpty()) {
            return empty(SkillType.READING);
        }
        var sorted = attempts.stream().sorted(Comparator.comparing(ReadingAttempt::getCreatedAt).reversed()).toList();
        List<Double> bands = sorted.stream().map(a -> ProgressMath.roundBand(1d + (a.getScorePercent() * 0.08d))).toList();
        double current = weightedRecentAverage(bands);
        double trend = trend(bands);
        Instant lastActivityAt = toInstant(sorted.getFirst().getCreatedAt());
        boolean stale = isStale(lastActivityAt);
        ConfidenceLevel confidence = confidenceFromCountAndFreshness(sorted.size(), stale);
        return new SkillEstimate(SkillType.READING, ProgressMath.roundBand(current), ProgressMath.round1(trend), confidence,
            lastActivityAt, sorted.size(), stale, List.of("based on recent reading attempts"));
    }

    private SkillEstimate estimateListening(Long userId) {
        List<MockTestSectionResult> sections =
            mockTestSectionResultRepository.findTop20BySessionUserIdAndSectionIgnoreCaseOrderByCreatedAtDesc(
                userId, "LISTENING");
        if (sections.isEmpty()) {
            return empty(SkillType.LISTENING);
        }
        var bands = sections.stream().map(MockTestSectionResult::getBandScore).filter(v -> v != null && v > 0).toList();
        if (bands.isEmpty()) {
            return empty(SkillType.LISTENING);
        }
        double current = weightedRecentAverage(bands);
        double trend = trend(bands);
        Instant lastActivityAt = toInstant(sections.getFirst().getCreatedAt());
        boolean stale = isStale(lastActivityAt);
        ConfidenceLevel confidence = confidenceFromCountAndFreshness(sections.size(), stale);
        return new SkillEstimate(SkillType.LISTENING, ProgressMath.roundBand(current), ProgressMath.round1(trend), confidence,
            lastActivityAt, sections.size(), stale, List.of("based on listening section results"));
    }

    private SkillEstimate estimateWriting(Long userId) {
        List<WritingSubmission> submissions =
            writingSubmissionRepository.findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(userId, WritingSubmissionStatus.EVALUATED);
        var bands = submissions.stream()
            .map(WritingSubmission::getOverallBand)
            .filter(band -> band != null && band > 0)
            .map(ProgressMath::roundBand)
            .toList();
        if (bands.isEmpty()) {
            return empty(SkillType.WRITING);
        }
        double current = weightedRecentAverage(bands);
        double trend = trend(bands);
        Instant lastActivityAt = toInstant(submissions.getFirst().getCreatedAt());
        boolean stale = isStale(lastActivityAt);
        ConfidenceLevel confidence = confidenceFromCountAndFreshness(submissions.size(), stale);
        return new SkillEstimate(SkillType.WRITING, ProgressMath.roundBand(current), ProgressMath.round1(trend), confidence,
            lastActivityAt, submissions.size(), stale, List.of("based on evaluated writing submissions"));
    }

    private SkillEstimate estimateSpeaking(Long userId) {
        List<SpeakingRecordingMetadata> recordings =
            speakingRecordingRepository.findTop20ByUserIdAndEvaluationResultIsNotNullOrderByCreatedAtDesc(userId);
        var bands = recordings.stream()
            .map(SpeakingRecordingMetadata::getEvaluationResult)
            .filter(result -> result != null && result.getOverallBand() != null)
            .map(result -> ProgressMath.roundBand(result.getOverallBand()))
            .toList();
        if (bands.isEmpty()) {
            return empty(SkillType.SPEAKING);
        }
        double current = weightedRecentAverage(bands);
        double trend = trend(bands);
        Instant lastActivityAt = toInstant(recordings.getFirst().getCreatedAt());
        boolean stale = isStale(lastActivityAt);
        ConfidenceLevel confidence = confidenceFromCountAndFreshness(recordings.size(), stale);
        return new SkillEstimate(SkillType.SPEAKING, ProgressMath.roundBand(current), ProgressMath.round1(trend), confidence,
            lastActivityAt, recordings.size(), stale, List.of("based on evaluated speaking recordings"));
    }

    private SkillEstimate empty(SkillType skillType) {
        return new SkillEstimate(skillType, null, 0d, ConfidenceLevel.LOW, null, 0, true, List.of("insufficient recent data"));
    }

    private double weightedRecentAverage(List<Double> values) {
        if (values.isEmpty()) {
            return 0;
        }
        double totalWeight = 0;
        double weighted = 0;
        for (int i = 0; i < values.size(); i++) {
            double weight = Math.max(1, values.size() - i);
            weighted += values.get(i) * weight;
            totalWeight += weight;
        }
        return weighted / totalWeight;
    }

    private double trend(List<Double> values) {
        if (values.size() < 2) {
            return 0;
        }
        int pivot = Math.max(1, values.size() / 2);
        List<Double> recent = new ArrayList<>(values.subList(0, pivot));
        List<Double> previous = new ArrayList<>(values.subList(pivot, values.size()));
        double recentAvg = recent.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double previousAvg = previous.stream().mapToDouble(Double::doubleValue).average().orElse(recentAvg);
        return recentAvg - previousAvg;
    }

    private boolean isStale(Instant timestamp) {
        return timestamp == null || timestamp.isBefore(Instant.now().minus(STALE_AFTER));
    }

    private Instant toInstant(LocalDateTime value) {
        return value == null ? null : value.toInstant(ZoneOffset.UTC);
    }

    private ConfidenceLevel confidenceFromCountAndFreshness(int count, boolean stale) {
        if (count >= 6 && !stale) {
            return ConfidenceLevel.HIGH;
        }
        if (count >= 3 && !stale) {
            return ConfidenceLevel.MEDIUM;
        }
        return ConfidenceLevel.LOW;
    }
}
