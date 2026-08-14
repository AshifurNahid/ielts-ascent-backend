package com.ieltsascent.backend.application.writing.core;

import com.ieltsascent.backend.application.common.CurrentUserProvider;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.writing.WritingSubmission;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.writing.WritingSubmissionRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WritingSubmissionServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private WritingPromptRepository writingPromptRepository;
    @Mock
    private WritingSubmissionRepository writingSubmissionRepository;
    @Mock
    private WritingEvaluationService writingEvaluationService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private WritingSubmissionService service;

    @Test
    void rejectsEvaluationAccessFromOtherUser() {
        Long requesterId = 1L;
        Long submissionId = 2L;

        when(writingSubmissionRepository.findByIdAndUserId(submissionId, requesterId))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findSubmissionOwnedByUser(requesterId, submissionId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Writing submission not found");
    }

    @Test
    void returnsSubmissionWhenOwnedByRequester() {
        Long requesterId = 1L;
        Long submissionId = 2L;
        WritingSubmission submission = new WritingSubmission();

        when(writingSubmissionRepository.findByIdAndUserId(submissionId, requesterId))
            .thenReturn(Optional.of(submission));

        assertThat(service.findSubmissionOwnedByUser(requesterId, submissionId)).isSameAs(submission);
    }
}
