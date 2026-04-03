package com.ieltsascent.backend.application.content;

import com.ieltsascent.backend.domain.content.ListeningAudio;
import com.ieltsascent.backend.domain.content.ReadingPassage;
import com.ieltsascent.backend.domain.content.VocabularyItem;
import com.ieltsascent.backend.infrastructure.persistence.ListeningAudioRepository;
import com.ieltsascent.backend.infrastructure.persistence.ReadingPassageRepository;
import com.ieltsascent.backend.infrastructure.persistence.VocabularyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ReadingPassageRepository readingPassageRepository;
    private final ListeningAudioRepository listeningAudioRepository;
    private final VocabularyItemRepository vocabularyItemRepository;

    public Page<ReadingPassage> listReadingPassages(Pageable pageable) {
        return readingPassageRepository.findAll(pageable);
    }

    public Page<ListeningAudio> listListeningAudios(Pageable pageable) {
        return listeningAudioRepository.findAll(pageable);
    }

    public Page<VocabularyItem> listVocabularyItems(Pageable pageable) {
        return vocabularyItemRepository.findAll(pageable);
    }
}
