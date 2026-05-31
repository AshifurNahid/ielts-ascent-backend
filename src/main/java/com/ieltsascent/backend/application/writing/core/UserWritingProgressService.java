package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.UserWritingProgress;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.writing.UserWritingProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserWritingProgressService {
    private final UserWritingProgressRepository progressRepository;
    private final WritingSubmissionRepository submissionRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public void recalculate(User user) {
        Long userId = user.getId();
        List<WritingSubmission> completed = submissionRepository.findTop20ByUserIdAndStatusOrderBySubmittedAtDesc(userId, WritingSubmissionStatus.EVALUATED);
        UserWritingProgress progress = progressRepository.findByUserId(userId).orElseGet(() -> {
            UserWritingProgress p = new UserWritingProgress();
            p.setUser(user);
            p.setTotalSubmissions(0L);
            return p;
        });

        progress.setTotalSubmissions(submissionRepository.countByUserIdAndStatus(userId, WritingSubmissionStatus.EVALUATED));
        if (!completed.isEmpty()) {
            List<Double> bands = completed.stream().map(WritingSubmission::getOverallBand).filter(Objects::nonNull).toList();
            double avg = bands.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double best = bands.stream().max(Comparator.naturalOrder()).orElse(0.0);
            double latest = Objects.isNull(completed.getFirst().getOverallBand()) ? 0.0 : completed.getFirst().getOverallBand();
            double trend = computeTrend(completed);

            Instant thirtyDaysAgo = Instant.now().minus(30, ChronoUnit.DAYS);
            List<WritingSubmission> last30 = submissionRepository.findByUserIdAndStatusAndSubmittedAtAfterOrderBySubmittedAtAsc(
                userId,
                WritingSubmissionStatus.EVALUATED,
                thirtyDaysAgo
            );
            List<Double> last30Bands = extractNonNullBands(last30);
            double improve30 = computeWindowImprovement(last30Bands);
            double netImprove30 = computeNetImprovement(last30Bands);
            double volatility30 = computeVolatility(last30Bands);

            progress.setAverageBand(round(avg));
            progress.setBestBand(round(best));
            progress.setLatestBand(round(latest));
            progress.setTrendValue(round(trend));
            progress.setLast30DayImprovement(round(improve30));
            progress.setLast30DayNetImprovement(round(netImprove30));
            progress.setLast30DayVolatility(round(volatility30));
        }
        progressRepository.save(progress);
    }

    public UserWritingProgress getByUserId(Long userId) {
        return progressRepository.findByUserId(userId).orElse(null);
    }

    public UserWritingProgress getCurrent() {
        return getByUserId(currentUserProvider.userId());
    }

    private double computeTrend(List<WritingSubmission> submissions) {
        List<Double> bands = extractNonNullBands(submissions);
        if (bands.size() < 2) {
            return 0.0;
        }
        return computeRegressionSlope(bands);
    }

    private List<Double> extractNonNullBands(List<WritingSubmission> submissions) {
        List<Double> bands = new ArrayList<>();
        for (WritingSubmission submission : submissions) {
            if (Objects.nonNull(submission.getOverallBand())) {
                bands.add(submission.getOverallBand());
            }
        }
        return bands;
    }

    private double computeWindowImprovement(List<Double> bands) {
        if (bands.size() < 2) {
            return 0.0;
        }
        int window = 3;
        if (bands.size() < window) {
            window = bands.size();
        }
        double firstAvg = average(bands.subList(0, window));
        double lastAvg = average(bands.subList(bands.size() - window, bands.size()));
        return lastAvg - firstAvg;
    }

    private double computeNetImprovement(List<Double> bands) {
        if (bands.size() < 2) {
            return 0.0;
        }
        return bands.getLast() - bands.getFirst();
    }

    private double computeRegressionSlope(List<Double> bands) {
        int n = bands.size();
        if (n < 2) {
            return 0.0;
        }

        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumXX = 0.0;

        for (int i = 0; i < n; i++) {
            double y = bands.get(i);
            sumX += i;
            sumY += y;
            sumXY += i * y;
            sumXX += (double) i * i;
        }

        double denominator = n * sumXX - sumX * sumX;
        if (denominator == 0.0) {
            return 0.0;
        }
        return (n * sumXY - sumX * sumY) / denominator;
    }

    private double computeVolatility(List<Double> bands) {
        if (bands.size() < 2) {
            return 0.0;
        }

        double mean = average(bands);
        double squaredDiffSum = 0.0;
        for (double band : bands) {
            double diff = band - mean;
            squaredDiffSum += diff * diff;
        }
        return Math.sqrt(squaredDiffSum / bands.size());
    }

    private double average(List<Double> values) {
        if (values.isEmpty()) {
            return 0.0;
        }
        return values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
