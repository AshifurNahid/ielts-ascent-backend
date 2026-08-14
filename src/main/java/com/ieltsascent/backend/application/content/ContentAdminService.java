package com.ieltsascent.backend.application.content;

import com.ieltsascent.backend.api.content.dto.AdminListeningRequest;
import com.ieltsascent.backend.api.content.dto.AdminSpeakingPromptRequest;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.SpeakingPrompt;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.SpeakingPromptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContentAdminService {
    private final ListeningAudioRepository listeningAudioRepository;
    private final SpeakingPromptRepository speakingPromptRepository;

    @Transactional
    public ListeningAudio createListening(AdminListeningRequest request) {
        ListeningAudio audio = new ListeningAudio();
        ContentAdminMapper.applyListeningRequest(audio, request);
        return listeningAudioRepository.save(audio);
    }

    @Transactional(readOnly = true)
    public Page<ListeningAudio> listListening(Pageable pageable) {
        return listeningAudioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public ListeningAudio getListening(Long id) {
        return findListening(id);
    }

    @Transactional
    public ListeningAudio updateListening(Long id, AdminListeningRequest request) {
        ListeningAudio audio = findListening(id);
        ContentAdminMapper.applyListeningRequest(audio, request);
        return listeningAudioRepository.save(audio);
    }

    @Transactional
    public void deleteListening(Long id) {
        listeningAudioRepository.delete(findListening(id));
    }

    @Transactional
    public SpeakingPrompt createSpeakingPrompt(AdminSpeakingPromptRequest request) {
        SpeakingPrompt prompt = new SpeakingPrompt();
        ContentAdminMapper.applySpeakingPromptRequest(prompt, request);
        return speakingPromptRepository.save(prompt);
    }

    @Transactional(readOnly = true)
    public Page<SpeakingPrompt> listSpeakingPrompts(Pageable pageable) {
        return speakingPromptRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public SpeakingPrompt getSpeakingPrompt(Long id) {
        return findSpeakingPrompt(id);
    }

    @Transactional
    public SpeakingPrompt updateSpeakingPrompt(Long id, AdminSpeakingPromptRequest request) {
        SpeakingPrompt prompt = findSpeakingPrompt(id);
        ContentAdminMapper.applySpeakingPromptRequest(prompt, request);
        return speakingPromptRepository.save(prompt);
    }

    @Transactional
    public void deleteSpeakingPrompt(Long id) {
        speakingPromptRepository.delete(findSpeakingPrompt(id));
    }

    private ListeningAudio findListening(Long id) {
        return listeningAudioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Listening audio not found"));
    }

    private SpeakingPrompt findSpeakingPrompt(Long id) {
        return speakingPromptRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Speaking prompt not found"));
    }
}

