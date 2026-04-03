package com.ieltsascent.backend.domain.practice;

public record TargetBand(Double value) {
    public static TargetBand unspecified() {
        return new TargetBand(null);
    }
}
