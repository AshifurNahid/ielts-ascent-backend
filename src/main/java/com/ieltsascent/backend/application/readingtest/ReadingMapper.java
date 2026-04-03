package com.ieltsascent.backend.application.readingtest;

import com.ieltsascent.backend.api.readingtest.dto.ReadingDtos;
import com.ieltsascent.backend.domain.readingtest.*;
import java.util.List;

public final class ReadingMapper {
    private ReadingMapper() {}

    public static ReadingDtos.ReadingPassageResponse toPassageResponse(ReadingPassage p) {
        return new ReadingDtos.ReadingPassageResponse(p.getId(), p.getTitle(), p.getContent(), p.getShortDescription(), p.getTopicTag(),
            p.getDifficulty(), p.getIeltsBandMin(), p.getIeltsBandMax(), p.getEstimatedReadingMinutes(), p.getPremium(), p.getStatus(), p.getCreatedAt(), p.getUpdatedAt());
    }

    public static ReadingDtos.ReadingQuestionResponse toQuestionResponse(ReadingQuestion q) {
        return new ReadingDtos.ReadingQuestionResponse(q.getId(), q.getPassage().getId(), q.getGroupNumber(), q.getType(), q.getPrompt(), q.getOptions(),
            q.getCorrectAnswer(), q.getExplanation(), q.getAnswerSourceHint(), q.getDifficulty(), q.getOrderIndex(), q.getPremium(), q.getStatus(), q.getTags(), q.getCreatedAt(), q.getUpdatedAt());
    }

    public static ReadingDtos.ReadingQuestionResponse toUserQuestionResponse(ReadingQuestion q) {
        return new ReadingDtos.ReadingQuestionResponse(q.getId(), q.getPassage().getId(), q.getGroupNumber(), q.getType(), q.getPrompt(), q.getOptions(),
            null, null, q.getAnswerSourceHint(), q.getDifficulty(), q.getOrderIndex(), q.getPremium(), q.getStatus(), q.getTags(), q.getCreatedAt(), q.getUpdatedAt());
    }

    public static ReadingDtos.ReadingTestResponse toTestResponse(ReadingTest t) {
        return new ReadingDtos.ReadingTestResponse(t.getId(), t.getTitle(), t.getDescription(), t.getTotalTimeMinutes(), t.getDifficulty(), t.getPremium(), t.getStatus(), t.getCreatedAt(), t.getUpdatedAt());
    }

    public static ReadingDtos.SkillProgressResponse toSkillResponse(UserReadingSkillProgress s) {
        return new ReadingDtos.SkillProgressResponse(s.getId(), s.getSkillType(), s.getSkillKey(), s.getTotalAttempts(), s.getTotalCorrect(),
            s.getRecentAttempts(), s.getRecentCorrect(), s.getAverageTimeSeconds(), s.getMasteryScore(), s.getWeakScore(), s.getStatus(), s.getLastPracticedAt(), s.getLastImprovedAt());
    }

    public static List<String> normalize(List<String> values) {
        if (values == null) return List.of();
        return values.stream().filter(v -> v != null && !v.isBlank()).map(String::trim).distinct().toList();
    }
}
