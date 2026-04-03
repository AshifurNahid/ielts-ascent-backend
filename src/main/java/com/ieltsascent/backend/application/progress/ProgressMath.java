package com.ieltsascent.backend.application.progress;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class ProgressMath {
    private ProgressMath() {}

    static double roundBand(Double value) {
        if (value == null) {
            return 0.0;
        }
        return BigDecimal.valueOf(value)
            .multiply(BigDecimal.valueOf(2))
            .setScale(0, RoundingMode.HALF_UP)
            .divide(BigDecimal.valueOf(2), 1, RoundingMode.HALF_UP)
            .doubleValue();
    }

    static Double progressPercent(Double current, Double target) {
        if (current == null || target == null || target <= 0) {
            return null;
        }
        return Math.min(100d, round1((current / target) * 100d));
    }

    static double round1(double value) {
        return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
