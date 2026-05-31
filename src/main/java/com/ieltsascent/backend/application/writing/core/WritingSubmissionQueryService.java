package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSuggestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingWeakPointRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingSubmissionQueryService {
    private final WritingSubmissionService writingSubmissionService;
    private final WritingWeakPointRepository writingWeakPointRepository;
    private final WritingSuggestionRepository writingSuggestionRepository;
    private final CurrentUserProvider currentUserProvider;

    public WritingDtos.SubmissionResponse fromSubmission(WritingSubmission submission) {
        return WritingDtos.SubmissionResponse.from(
            submission,
            writingWeakPointRepository.findBySubmissionId(submission.getId()),
            writingSuggestionRepository.findBySubmissionId(submission.getId())
        );
    }

    @Transactional(readOnly = true)
    public WritingDtos.SubmissionResponse getOwned(Long userId, Long submissionId) {
        return fromSubmission(writingSubmissionService.findSubmissionOwnedByUser(userId, submissionId));
    }

    @Transactional(readOnly = true)
    public WritingDtos.SubmissionResponse getOwned(Long submissionId) {
        return getOwned(currentUserProvider.userId(), submissionId);
    }

    @Transactional(readOnly = true)
    public Page<WritingDtos.SubmissionResponse> history(Long userId, Pageable pageable) {
        Page<WritingSubmission> page = writingSubmissionService.history(userId, pageable);
        List<Long> submissionIds = page.stream().map(WritingSubmission::getId).toList();
        if (submissionIds.isEmpty()) {
            return page.map(s -> WritingDtos.SubmissionResponse.from(s, List.of(), List.of()));
        }

        Map<Long, List<WritingWeakPoint>> weakPointsBySubmission = writingWeakPointRepository.findBySubmissionIdIn(submissionIds)
            .stream()
            .filter(wp -> wp.getSubmission() != null)
            .collect(Collectors.groupingBy(wp -> wp.getSubmission().getId()));
        Map<Long, List<WritingSuggestion>> suggestionsBySubmission = writingSuggestionRepository.findBySubmissionIdIn(submissionIds)
            .stream()
            .filter(s -> s.getSubmission() != null)
            .collect(Collectors.groupingBy(s -> s.getSubmission().getId()));

        return page.map(submission -> WritingDtos.SubmissionResponse.from(
            submission,
            weakPointsBySubmission.getOrDefault(submission.getId(), List.of()),
            suggestionsBySubmission.getOrDefault(submission.getId(), List.of())
        ));
    }

    @Transactional(readOnly = true)
    public Page<WritingDtos.SubmissionResponse> history(Pageable pageable) {
        return history(currentUserProvider.userId(), pageable);
    }
}


