package com.ieltsascent.backend.application.ai;

import java.util.Map;

public record ExamPrediction(double overallBand, Map<String, Double> skillBands, String summary) {
}
