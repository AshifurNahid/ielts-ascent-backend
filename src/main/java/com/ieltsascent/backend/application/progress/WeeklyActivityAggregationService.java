package com.ieltsascent.backend.application.progress;

import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.DailyActivity;
import com.ieltsascent.backend.api.progress.dto.ProgressDashboardDtos.WeeklyActivity;
import com.ieltsascent.backend.domain.practice.PracticeSession;
import com.ieltsascent.backend.infrastructure.persistence.PracticeSessionRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeeklyActivityAggregationService {
    private static final int DEFAULT_SESSION_MINUTES = 20;

    private final PracticeSessionRepository practiceSessionRepository;

    public WeeklyActivity aggregate(UUID userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate fromDate = today.minusDays(6);
        Instant from = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<PracticeSession> sessions = practiceSessionRepository.findByUser_IdAndCompletedAtBetween(userId, from, to);

        Map<LocalDate, Integer> byDay = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            byDay.put(fromDate.plusDays(i), 0);
        }

        for (PracticeSession session : sessions) {
            Instant completedAt = session.getCompletedAt();
            if (completedAt == null) {
                continue;
            }
            LocalDate day = completedAt.atZone(ZoneOffset.UTC).toLocalDate();
            if (!byDay.containsKey(day)) {
                continue;
            }
            int minutes = estimateMinutes(session);
            byDay.put(day, byDay.get(day) + minutes);
        }

        List<DailyActivity> days = new ArrayList<>();
        int total = 0;
        for (Map.Entry<LocalDate, Integer> entry : byDay.entrySet()) {
            days.add(new DailyActivity(
                entry.getKey().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                entry.getValue()
            ));
            total += entry.getValue();
        }

        return new WeeklyActivity(total, days);
    }

    private int estimateMinutes(PracticeSession session) {
        if (session.getStartedAt() != null && session.getCompletedAt() != null &&
            session.getCompletedAt().isAfter(session.getStartedAt())) {
            long minutes = Duration.between(session.getStartedAt(), session.getCompletedAt()).toMinutes();
            if (minutes > 0 && minutes < 300) {
                return (int) minutes;
            }
        }
        return DEFAULT_SESSION_MINUTES;
    }
}
