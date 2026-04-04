package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.api.writing.dto.WritingDtos;
import com.ieltsascent.backend.domain.writing.WritingSuggestion;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingWeakPoint;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSuggestionRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingWeakPointRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WritingSubmissionQueryService {
    private final WritingSubmissionService writingSubmissionService;
    private final WritingWeakPointRepository writingWeakPointRepository;
    private final WritingSuggestionRepository writingSuggestionRepository;

    public WritingDtos.SubmissionResponse fromSubmission(WritingSubmission submission) {
        return WritingDtos.SubmissionResponse.from(
            submission,
            writingWeakPointRepository.findBySubmissionId(submission.getId()),
            writingSuggestionRepository.findBySubmissionId(submission.getId())
        );
    }

    public WritingDtos.SubmissionResponse getOwned(UUID userId, UUID submissionId) {
        return fromSubmission(writingSubmissionService.getOwned(userId, submissionId));
    }

    public Page<WritingDtos.SubmissionResponse> history(UUID userId, Pageable pageable) {
        Page<WritingSubmission> page = writingSubmissionService.history(userId, pageable);
        List<UUID> submissionIds = page.stream().map(WritingSubmission::getId).toList();
        if (submissionIds.isEmpty()) {
            return page.map(s -> WritingDtos.SubmissionResponse.from(s, List.of(), List.of()));
        }

        Map<UUID, List<WritingWeakPoint>> weakPointsBySubmission = writingWeakPointRepository.findBySubmissionIdIn(submissionIds)
            .stream()
            .collect(Collectors.groupingBy(wp -> wp.getSubmission().getId()));
        Map<UUID, List<WritingSuggestion>> suggestionsBySubmission = writingSuggestionRepository.findBySubmissionIdIn(submissionIds)
            .stream()
            .collect(Collectors.groupingBy(s -> s.getSubmission().getId()));

        return page.map(submission -> WritingDtos.SubmissionResponse.from(
            submission,
            weakPointsBySubmission.getOrDefault(submission.getId(), List.of()),
            suggestionsBySubmission.getOrDefault(submission.getId(), List.of())
        ));
    }
}


