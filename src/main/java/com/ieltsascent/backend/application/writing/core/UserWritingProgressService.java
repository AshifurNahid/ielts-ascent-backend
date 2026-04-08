package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.UserWritingProgress;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.writing.UserWritingProgressRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
            double improve30 = 0.0;
            if (last30.size() >= 2) {
                improve30 = value(last30.getLast().getOverallBand()) - value(last30.getFirst().getOverallBand());
            }
            progress.setAverageBand(round(avg));
            progress.setBestBand(round(best));
            progress.setLatestBand(round(latest));
            progress.setTrendValue(round(trend));
            progress.setLast30DayImprovement(round(improve30));
        }
        progressRepository.save(progress);
    }

    public UserWritingProgress getByUserId(Long userId) {
        return progressRepository.findByUserId(userId).orElse(null);
    }

    private double computeTrend(List<WritingSubmission> submissions) {
        if (submissions.size() < 2) {
            return 0.0;
        }
        double oldest = value(submissions.getLast().getOverallBand());
        double newest = value(submissions.getFirst().getOverallBand());
        return newest - oldest;
    }

    private double value(Double value) {
        return Objects.isNull(value) ? 0.0 : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
