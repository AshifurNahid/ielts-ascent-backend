package com.ieltsascent.backend.application.writing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WritingPredictionCalculatorTest {

    private final WritingPredictionCalculator calculator = new WritingPredictionCalculator();

    @Test
    void calculatesHighConfidenceForStableHistory() {
        List<WritingEvaluationSnapshot> snapshots = List.of(
            snapshot(1, 6.5),
            snapshot(3, 6.5),
            snapshot(5, 6.5),
            snapshot(7, 6.5),
            snapshot(9, 6.5),
            snapshot(11, 6.5),
            snapshot(13, 6.5),
            snapshot(15, 6.5)
        );

        WritingPredictionResult result = calculator.calculate(snapshots);

        assertThat(result.currentBand()).isEqualTo(6.5);
        assertThat(result.confidence()).isEqualTo(WritingPredictionConfidence.HIGH);
        assertThat(result.trend()).isEqualTo(WritingPredictionTrend.FLAT);
    }

    @Test
    void detectsUpwardTrendForRecentImprovement() {
        List<WritingEvaluationSnapshot> snapshots = List.of(
            snapshot(20, 5.0),
            snapshot(15, 5.4),
            snapshot(10, 5.7),
            snapshot(5, 6.0),
            snapshot(1, 6.3)
        );

        WritingPredictionResult result = calculator.calculate(snapshots);

        assertThat(result.trend()).isEqualTo(WritingPredictionTrend.UPWARD);
        assertThat(result.currentBand()).isGreaterThan(5.5);
    }

    private WritingEvaluationSnapshot snapshot(long daysAgo, double overallBand) {
        Instant evaluatedAt = Instant.now().minus(daysAgo, ChronoUnit.DAYS);
        return new WritingEvaluationSnapshot(
            evaluatedAt,
            overallBand,
            overallBand,
            overallBand,
            overallBand,
            overallBand,
            false,
            false
        );
    }
}
