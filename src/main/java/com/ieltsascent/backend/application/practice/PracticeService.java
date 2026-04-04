package com.ieltsascent.backend.application.practice;

import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;

import com.ieltsascent.backend.application.ai.AiSpeakingEvaluationClient;
import com.ieltsascent.backend.domain.auth.User;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.domain.practice.SpeakingEvaluationResult;
import com.ieltsascent.backend.domain.practice.SpeakingRecordingMetadata;
import com.ieltsascent.backend.domain.practice.PracticeSession;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingPromptRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingRecordingRepository;
import com.ieltsascent.backend.infrastructure.persistence.UserRepository;
import com.ieltsascent.backend.infrastructure.persistence.PracticeSessionRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PracticeService {
    private final SpeakingPromptRepository speakingPromptRepository;
    private final SpeakingRecordingRepository speakingRecordingRepository;
    private final UserRepository userRepository;
    private final AiSpeakingEvaluationClient aiSpeakingEvaluationClient;
    private final PracticeSessionRepository practiceSessionRepository;
    private final ListeningAudioRepository listeningAudioRepository;

    public Page<SpeakingPrompt> listSpeakingPrompts(Pageable pageable) {
        return speakingPromptRepository.findAll(pageable);
    }

    public SpeakingRecordingMetadata submitSpeaking(UUID userId, UUID promptId, String audioUrl, Integer duration) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        SpeakingPrompt prompt = speakingPromptRepository.findById(promptId)
            .orElseThrow(() -> new ResourceNotFoundException("Prompt not found"));

        SpeakingRecordingMetadata recording = new SpeakingRecordingMetadata();
        recording.setUser(user);
        recording.setPrompt(prompt);
        recording.setAudioUrl(audioUrl);
        recording.setDurationSeconds(duration);

        SpeakingEvaluationResult evaluationResult = aiSpeakingEvaluationClient.evaluateSpeaking(recording);
        recording.setEvaluationResult(evaluationResult);

        SpeakingRecordingMetadata savedRecording = speakingRecordingRepository.save(recording);
        recordSpeakingCompletion(userId, promptId);
        return savedRecording;
    }

    public PracticeSession recordListeningCompletion(UUID userId, UUID taskId) {
        ensureListeningTaskExists(taskId);
        return recordPracticeCompletion(userId, "LISTENING", taskId);
    }

    public PracticeSession recordSpeakingCompletion(UUID userId, UUID promptId) {
        return recordPracticeCompletion(userId, "SPEAKING", promptId);
    }

    public Page<PracticeSession> listHistory(
        UUID userId,
        String skillType,
        Instant from,
        Instant to,
        Pageable pageable
    ) {
        userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return practiceSessionRepository.searchSessions(userId, skillType, from, to, pageable);
    }

    private PracticeSession recordPracticeCompletion(UUID userId, String skillType, UUID taskId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        PracticeSession session = new PracticeSession();
        session.setUser(user);
        session.setSkillType(skillType);
        session.setTaskId(taskId);
        session.setStartedAt(Instant.now());
        session.setCompletedAt(Instant.now());
        return practiceSessionRepository.save(session);
    }


    private void ensureListeningTaskExists(UUID taskId) {
        if (!listeningAudioRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Listening audio not found");
        }
    }
}
