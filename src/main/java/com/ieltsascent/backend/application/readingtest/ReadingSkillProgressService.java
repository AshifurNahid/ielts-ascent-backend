package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.domain.readingtest.*;
import com.ieltsascent.backend.infrastructure.persistence.reading.UserReadingSkillProgressRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingSkillProgressService {
    private final UserReadingSkillProgressRepository progressRepository;

    @Transactional
    public List<UserReadingSkillProgress> updateAfterAttempt(UUID userId, List<ReadingSkillUpdateEvent> events) {
        List<UserReadingSkillProgress> updated = new ArrayList<>();
        for (ReadingSkillUpdateEvent event : events) {
            UserReadingSkillProgress progress = progressRepository
                .findByUserIdAndSkillTypeAndSkillKey(userId, event.skillType(), event.skillKey())
                .orElseGet(() -> newProgress(userId, event.skillType(), event.skillKey()));

            double previousMastery = progress.getMasteryScore();
            progress.setTotalAttempts(progress.getTotalAttempts() + 1);
            progress.setTotalCorrect(progress.getTotalCorrect() + (event.correct() ? 1 : 0));

            int newRecentAttempts = Math.min(progress.getRecentAttempts() + 1, 20);
            int decayedRecentCorrect = (int) Math.floor(progress.getRecentCorrect() * 0.85);
            progress.setRecentAttempts(newRecentAttempts);
            progress.setRecentCorrect(Math.min(newRecentAttempts, decayedRecentCorrect + (event.correct() ? 1 : 0)));

            double averagedTime = progress.getAverageTimeSeconds() <= 0
                ? event.timeSpentSeconds()
                : (progress.getAverageTimeSeconds() * 0.8) + (event.timeSpentSeconds() * 0.2);
            progress.setAverageTimeSeconds(averagedTime);

            double lifetimeAccuracy = safeDivide(progress.getTotalCorrect(), progress.getTotalAttempts());
            double recentAccuracy = safeDivide(progress.getRecentCorrect(), progress.getRecentAttempts());
            double timePenalty = event.expectedTimeSeconds() <= 0 ? 0 : Math.min(20, (Math.max(0, averagedTime - event.expectedTimeSeconds()) / event.expectedTimeSeconds()) * 20);

            double mastery = clamp((recentAccuracy * 0.65 + lifetimeAccuracy * 0.35) * 100 - timePenalty);
            progress.setMasteryScore(mastery);
            progress.setWeakScore(100 - mastery);
            progress.setStatus(resolveStatus(mastery, mastery - previousMastery));
            progress.setLastPracticedAt(Instant.now());
            if (mastery > previousMastery + 2) {
                progress.setLastImprovedAt(Instant.now());
            }
            updated.add(progressRepository.save(progress));
        }
        return updated;
    }

    @Transactional(readOnly = true)
    public List<UserReadingSkillProgress> getProgress(UUID userId) {
        return progressRepository.findByUserIdOrderByWeakScoreDesc(userId);
    }

    private static UserReadingSkillProgress newProgress(UUID userId, ReadingSkillType skillType, String skillKey) {
        UserReadingSkillProgress p = new UserReadingSkillProgress();
        p.setUserId(userId);
        p.setSkillType(skillType);
        p.setSkillKey(skillKey);
        return p;
    }

    private static double safeDivide(int a, int b) { return b <= 0 ? 0 : a * 1.0 / b; }
    private static double clamp(double v) { return Math.max(0, Math.min(100, v)); }

    private static ReadingSkillStatus resolveStatus(double mastery, double delta) {
        if (mastery >= 85) return ReadingSkillStatus.MASTERED;
        if (mastery < 55) return ReadingSkillStatus.WEAK;
        if (delta > 3) return ReadingSkillStatus.IMPROVING;
        return ReadingSkillStatus.STABLE;
    }

    public record ReadingSkillUpdateEvent(ReadingSkillType skillType, String skillKey, boolean correct, int timeSpentSeconds, int expectedTimeSeconds) {}
}
