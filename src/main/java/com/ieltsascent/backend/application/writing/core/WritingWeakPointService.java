package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import com.ieltsascent.backend.domain.writing.WeakPointSeverity;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingWeakPointRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WritingWeakPointService {
    private final WritingWeakPointRepository writingWeakPointRepository;

    public List<RecurringWeakPointSummary> recurringWeakPoints(Long userId) {
        List<WritingWeakPoint> recent = writingWeakPointRepository.findTop100ByUserIdOrderByCreatedAtDesc(userId);
        Map<String, List<WritingWeakPoint>> grouped = recent.stream().collect(Collectors.groupingBy(w -> w.getCategory().name()));
        return grouped.entrySet().stream()
            .map(e -> new RecurringWeakPointSummary(
                e.getKey(),
                e.getValue().size(),
                e.getValue().stream().map(WritingWeakPoint::getSeverity).max(Comparator.naturalOrder()).orElse(null)
            ))
            .sorted(Comparator.comparingLong(RecurringWeakPointSummary::frequency).reversed())
            .toList();
    }

    public record RecurringWeakPointSummary(String category, long frequency, WeakPointSeverity maxSeverity) {
    }
}
