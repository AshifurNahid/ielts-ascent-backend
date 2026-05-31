package com.ieltsascent.backend.application.practice;

import com.ieltsascent.backend.api.common.PageResponse;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.application.content.ContentService;
import com.ieltsascent.backend.application.practice.dto.PracticeCompletionDto;
import com.ieltsascent.backend.application.practice.dto.PracticeListeningTaskDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSessionItemDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSpeakingPromptDto;
import com.ieltsascent.backend.application.practice.dto.PracticeSpeakingSubmissionDto;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PracticeApiService {
    private final PracticeService practiceService;
    private final ContentService contentService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public PageResponse<PracticeListeningTaskDto> listeningTasks(Pageable pageable) {
        Page<PracticeListeningTaskDto> page = contentService.listListeningAudios(pageable).map(PracticeListeningTaskDto::from);
        return PageResponse.from(page);
    }

    @Transactional
    public PracticeCompletionDto submitListening(Long userId, Long taskId) {
        return PracticeCompletionDto.from(practiceService.recordListeningCompletion(userId, taskId));
    }

    @Transactional
    public PracticeCompletionDto submitListening(Long taskId) {
        return submitListening(currentUserProvider.userId(), taskId);
    }

    @Transactional(readOnly = true)
    public PageResponse<PracticeSpeakingPromptDto> speakingPrompts(Pageable pageable) {
        Page<PracticeSpeakingPromptDto> page = practiceService.listSpeakingPrompts(pageable).map(PracticeSpeakingPromptDto::from);
        return PageResponse.from(page);
    }

    @Transactional
    public PracticeSpeakingSubmissionDto submitSpeaking(Long userId, Long promptId, String audioUrl, Integer durationSeconds) {
        return PracticeSpeakingSubmissionDto.from(practiceService.submitSpeaking(userId, promptId, audioUrl, durationSeconds));
    }

    @Transactional
    public PracticeSpeakingSubmissionDto submitSpeaking(Long promptId, String audioUrl, Integer durationSeconds) {
        return submitSpeaking(currentUserProvider.userId(), promptId, audioUrl, durationSeconds);
    }

    @Transactional(readOnly = true)
    public PageResponse<PracticeSessionItemDto> history(
        Long userId,
        String skillType,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    ) {
        Instant fromInstant = from == null ? null : from.toInstant(ZoneOffset.UTC);
        Instant toInstant = to == null ? null : to.toInstant(ZoneOffset.UTC);
        Page<PracticeSessionItemDto> page = practiceService.listHistory(userId, skillType, fromInstant, toInstant, pageable)
            .map(PracticeSessionItemDto::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<PracticeSessionItemDto> history(
        String skillType,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    ) {
        return history(currentUserProvider.userId(), skillType, from, to, pageable);
    }
}

