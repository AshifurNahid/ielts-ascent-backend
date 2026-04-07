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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class WritingSubmissionService {
    private final UserRepository userRepository;
    private final WritingPromptRepository writingPromptRepository;
    private final WritingSubmissionRepository writingSubmissionRepository;
    private final WritingEvaluationService writingEvaluationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public WritingSubmission start(Long userId, Long promptId, boolean timedMode) {
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
    public WritingSubmission saveDraft(Long userId, Long submissionId, String essayText) {
        WritingSubmission submission = getOwned(userId, submissionId);
        ensureDraftAllowed(submission);
        submission.setEssayText(essayText);
        submission.setWordCount(countWords(essayText));
        submission.setStatus(WritingSubmissionStatus.DRAFT);
        return writingSubmissionRepository.save(submission);
    }

    @Transactional
    public WritingSubmission submit(Long userId, Long submissionId, String essayText) {
        WritingSubmission submission = getOwned(userId, submissionId);
        ensureSubmitAllowed(submission);
        submission.setEssayText(essayText);
        submission.setWordCount(countWords(essayText));
        submission.setSubmittedAt(Instant.now());
        submission.setStatus(WritingSubmissionStatus.EVALUATION_PENDING);
        if (submission.getStartedAt() != null) {
            submission.setDurationSeconds(Duration.between(submission.getStartedAt(), submission.getSubmittedAt()).toSeconds());
        }
        submission.setAiSummary("Evaluation in progress.");
        submission = writingSubmissionRepository.save(submission);
        writingEvaluationService.clearFeedback(submission.getId());
        eventPublisher.publishEvent(new WritingSubmissionSubmittedEvent(submission.getId()));
        return submission;
    }

    public WritingSubmission getOwned(Long userId, Long submissionId) {
        return writingSubmissionRepository.findByIdAndUserId(submissionId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("Writing submission not found"));
    }

    public Page<WritingSubmission> history(Long userId, Pageable pageable) {
        return writingSubmissionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private int countWords(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }
        return text.trim().split("\\s+").length;
    }

    private void ensureDraftAllowed(WritingSubmission submission) {
        if (submission.getStatus() == WritingSubmissionStatus.SUBMITTED
            || submission.getStatus() == WritingSubmissionStatus.EVALUATION_PENDING
            || submission.getStatus() == WritingSubmissionStatus.EVALUATED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Draft cannot be modified after submission");
        }
    }

    private void ensureSubmitAllowed(WritingSubmission submission) {
        if (submission.getStatus() == WritingSubmissionStatus.SUBMITTED
            || submission.getStatus() == WritingSubmissionStatus.EVALUATION_PENDING
            || submission.getStatus() == WritingSubmissionStatus.EVALUATED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Submission is already finalized");
        }
    }
}
