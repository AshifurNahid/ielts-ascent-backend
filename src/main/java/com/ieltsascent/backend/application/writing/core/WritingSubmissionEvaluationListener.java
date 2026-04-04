package com.ieltsascent.backend.application.writing.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class WritingSubmissionEvaluationListener {
    private final WritingEvaluationService writingEvaluationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSubmitted(WritingSubmissionSubmittedEvent event) {
        try {
            writingEvaluationService.processPendingSubmission(event.submissionId());
        } catch (Exception ex) {
            log.error("Async evaluation failed for submission {}", event.submissionId(), ex);
        }
    }
}


