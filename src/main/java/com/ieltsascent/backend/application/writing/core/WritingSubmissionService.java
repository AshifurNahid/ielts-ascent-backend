package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.writing.WritingPrompt;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.domain.writing.WritingSubmissionStatus;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WritingSubmissionService {
    private final UserRepository userRepository;
    private final WritingPromptRepository writingPromptRepository;
    private final WritingSubmissionRepository writingSubmissionRepository;
    private final WritingEvaluationService writingEvaluationService;

    @Transactional
    public WritingSubmission start(UUID userId, UUID promptId, boolean timedMode) {
        User user = findUser(userId);
        WritingPrompt prompt = writingPromptRepository.findById(promptId)
            .orElseThrow(() -> new ResourceNotFoundException("Writing prompt not found"));
        WritingSubmission submission = new WritingSubmission();
        submission.setUser(user);
        submission.setPrompt(prompt);
        submission.setTaskType(prompt.getTaskType());
        submission.setEssayText("");
        submission.setWordCount(0);
        submission.setTimedMode(timedMode);
        submission.setStartedAt(Instant.now());
        submission.setStatus(WritingSubmissionStatus.STARTED);
        return writingSubmissionRepository.save(submission);
    }

    @Transactional
    public WritingSubmission saveDraft(UUID userId, UUID submissionId, String essayText) {
        WritingSubmission submission = getOwned(userId, submissionId);
        submission.setEssayText(essayText);
        submission.setWordCount(countWords(essayText));
        submission.setStatus(WritingSubmissionStatus.DRAFT);
        return writingSubmissionRepository.save(submission);
    }

    @Transactional
    public WritingSubmission submit(UUID userId, UUID submissionId, String essayText) {
        WritingSubmission submission = getOwned(userId, submissionId);
        submission.setEssayText(essayText);
        submission.setWordCount(countWords(essayText));
        submission.setSubmittedAt(Instant.now());
        submission.setStatus(WritingSubmissionStatus.SUBMITTED);
        if (submission.getStartedAt() != null) {
            submission.setDurationSeconds(Duration.between(submission.getStartedAt(), submission.getSubmittedAt()).toSeconds());
        }
        writingSubmissionRepository.save(submission);
        writingEvaluationService.evaluateAndPersist(submission);
        return writingSubmissionRepository.save(submission);
    }

    public WritingSubmission getOwned(UUID userId, UUID submissionId) {
        return writingSubmissionRepository.findByIdAndUserId(submissionId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Writing submission not found"));
    }

    public Page<WritingSubmission> history(UUID userId, Pageable pageable) {
        return writingSubmissionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }
}
