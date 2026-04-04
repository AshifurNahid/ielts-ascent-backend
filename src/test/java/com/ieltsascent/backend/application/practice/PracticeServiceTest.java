package com.ieltsascent.backend.application.practice;

import com.ieltsascent.backend.application.ai.AiSpeakingEvaluationClient;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.domain.practice.PracticeSession;
import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.PracticeSessionRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingRecordingRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PracticeServiceTest {
    @Mock
    private SpeakingPromptRepository speakingPromptRepository;
    @Mock
    private SpeakingRecordingRepository speakingRecordingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AiSpeakingEvaluationClient aiSpeakingEvaluationClient;
    @Mock
    private PracticeSessionRepository practiceSessionRepository;
    @Mock
    private ListeningAudioRepository listeningAudioRepository;

    @InjectMocks
    private PracticeService practiceService;

    @Test
    void recordsListeningCompletionWithTaskId() {
        UUID userId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        User user = new User();

        when(listeningAudioRepository.existsById(taskId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(practiceSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PracticeSession session = practiceService.recordListeningCompletion(userId, taskId);

        assertThat(session.getTaskId()).isEqualTo(taskId);
        assertThat(session.getSkillType()).isEqualTo("LISTENING");
        assertThat(session.getCompletedAt()).isNotNull();
    }


    @Test
    void recordsSpeakingCompletionOnSubmit() {
        UUID userId = UUID.randomUUID();
        UUID promptId = UUID.randomUUID();
        User user = new User();
        SpeakingPrompt prompt = new SpeakingPrompt();
        SpeakingEvaluationResult evaluationResult = new SpeakingEvaluationResult();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(speakingPromptRepository.findById(promptId)).thenReturn(Optional.of(prompt));
        when(aiSpeakingEvaluationClient.evaluateSpeaking(any())).thenReturn(evaluationResult);
        when(speakingRecordingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(practiceSessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SpeakingRecordingMetadata recording = practiceService.submitSpeaking(userId, promptId, "audio", 60);

        assertThat(recording.getEvaluationResult()).isSameAs(evaluationResult);
        ArgumentCaptor<PracticeSession> sessionCaptor = ArgumentCaptor.forClass(PracticeSession.class);
        verify(practiceSessionRepository).save(sessionCaptor.capture());
        assertThat(sessionCaptor.getValue().getSkillType()).isEqualTo("SPEAKING");
        assertThat(sessionCaptor.getValue().getTaskId()).isEqualTo(promptId);
    }

    @Test
    void returnsPracticeHistory() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        PracticeSession session = new PracticeSession();
        Page<PracticeSession> page = new PageImpl<>(java.util.List.of(session), PageRequest.of(0, 10), 1);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(practiceSessionRepository.searchSessions(userId, null, null, null, PageRequest.of(0, 10)))
            .thenReturn(page);

        Page<PracticeSession> result = practiceService.listHistory(userId, null, null, null, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void rejectsUnknownListeningTask() {
        UUID userId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        when(listeningAudioRepository.existsById(taskId)).thenReturn(false);

        assertThatThrownBy(() -> practiceService.recordListeningCompletion(userId, taskId))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Listening audio not found");

        verify(practiceSessionRepository, never()).save(any());
    }
}
