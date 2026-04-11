package com.ieltsascent.backend.application.content;

import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ListeningAudioRepository listeningAudioRepository;

    public Page<ListeningAudio> listListeningAudios(Pageable pageable) {
        return listeningAudioRepository.findAll(pageable);
    }
}
