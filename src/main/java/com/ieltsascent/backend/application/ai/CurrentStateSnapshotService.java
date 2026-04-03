package com.ieltsascent.backend.application.ai;

import com.ieltsascent.backend.domain.assessment.DiagnosticResult;
import com.ieltsascent.backend.domain.assessment.MicroSkillScore;
import com.ieltsascent.backend.infrastructure.persistence.DiagnosticResultRepository;
import com.ieltsascent.backend.infrastructure.persistence.MicroSkillScoreRepository;
import com.ieltsascent.backend.infrastructure.persistence.TaskCompletionRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentStateSnapshotService {
    private final DiagnosticResultRepository diagnosticResultRepository;
    private final MicroSkillScoreRepository microSkillScoreRepository;
    private final TaskCompletionRepository taskCompletionRepository;
    private final UserRepository userRepository;

    public CurrentStateSnapshot buildSnapshot(UUID userId) {
        Map<String, Integer> microSkillScores = new HashMap<>();
        DiagnosticResult latestResult = diagnosticResultRepository.findFirstBySessionUserIdOrderByCreatedAtDesc(userId)
            .orElse(null);
        if (latestResult != null) {
            List<MicroSkillScore> scores = microSkillScoreRepository.findByDiagnosticResultId(latestResult.getId());
            scores.forEach(score -> microSkillScores.put(score.getSkillName(), score.getScore()));
        }

        Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
        int weeklyMinutes = taskCompletionRepository.findByUserIdAndCompletedAtAfter(userId, oneWeekAgo).stream()
            .mapToInt(completion -> completion.getStudyTask().getEstimatedMinutes())
            .sum();
        double currentBand = latestResult != null && latestResult.getOverallBand() != null
            ? latestResult.getOverallBand()
            : userRepository.findById(userId)
                .map(user -> user.getProfile().getTargetBand())
                .orElse(0.0);
        return new CurrentStateSnapshot(microSkillScores, weeklyMinutes, currentBand);
    }
}
