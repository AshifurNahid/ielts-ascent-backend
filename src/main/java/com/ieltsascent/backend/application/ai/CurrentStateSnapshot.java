package com.ieltsascent.backend.application.ai;

import java.util.Map;

public record CurrentStateSnapshot(Map<String, Integer> microSkillScores, int weeklyMinutes, double currentBand) {
}
