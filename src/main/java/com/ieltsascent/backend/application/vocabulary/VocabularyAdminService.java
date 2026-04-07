package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.application.common.exception.ResourceNotFoundException;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import com.ieltsascent.backend.domain.vocabulary.VocabularyLevel;
import com.ieltsascent.backend.domain.vocabulary.VocabularyStatus;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyExampleRepository;
import com.ieltsascent.backend.infrastructure.persistence.vocabulary.VocabularyWordRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabularyAdminService {
    private final VocabularyWordRepository vocabularyWordRepository;
    private final VocabularyExampleRepository vocabularyExampleRepository;

    @Transactional
    public VocabularyWord createWord(VocabularyDtos.VocabularyWordUpsertRequest request) {
        validateBandRange(request.ieltsBandMin(), request.ieltsBandMax());
        VocabularyWord word = new VocabularyWord();
        applyWordRequest(word, request);
        return vocabularyWordRepository.save(word);
    }

    @Transactional
    public VocabularyWord updateWord(Long id, VocabularyDtos.VocabularyWordUpsertRequest request) {
        validateBandRange(request.ieltsBandMin(), request.ieltsBandMax());
        VocabularyWord word = findWord(id);
        applyWordRequest(word, request);
        return vocabularyWordRepository.save(word);
    }

    @Transactional
    public VocabularyWord updateStatus(Long id, VocabularyStatus status) {
        VocabularyWord word = findWord(id);
        word.setStatus(status);
        return vocabularyWordRepository.save(word);
    }

    @Transactional(readOnly = true)
    public Page<VocabularyWord> listWords(
        VocabularyStatus status,
        VocabularyLevel level,
        Boolean premium,
        String tag,
        String query,
        Pageable pageable
    ) {
        return vocabularyWordRepository.searchAdmin(
            status,
            level,
            premium,
            normalizeQuery(tag),
            normalizeQuery(query),
            pageable
        );
    }

    @Transactional(readOnly = true)
    public VocabularyWord getWord(Long id) {
        return vocabularyWordRepository.findDetailedById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));
    }

    @Transactional
    public VocabularyExample addExample(Long wordId, VocabularyDtos.VocabularyExampleRequest request) {
        VocabularyWord word = findWord(wordId);
        VocabularyExample example = new VocabularyExample();
        example.setType(request.type());
        example.setSentence(request.sentence().trim());
        example.setAiGenerated(request.aiGenerated());
        example.setApproved(request.approved());
        word.addExample(example);
        vocabularyWordRepository.save(word);
        return example;
    }

    @Transactional
    public VocabularyExample updateExample(Long exampleId, VocabularyDtos.VocabularyExampleRequest request) {
        VocabularyExample example = vocabularyExampleRepository.findById(exampleId)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary example not found"));
        example.setType(request.type());
        example.setSentence(request.sentence().trim());
        example.setAiGenerated(request.aiGenerated());
        example.setApproved(request.approved());
        return vocabularyExampleRepository.save(example);
    }

    @Transactional
    public void deleteExample(Long exampleId) {
        VocabularyExample example = vocabularyExampleRepository.findById(exampleId)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary example not found"));
        vocabularyExampleRepository.delete(example);
    }

    private VocabularyWord findWord(Long id) {
        return vocabularyWordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vocabulary word not found"));
    }

    private static void applyWordRequest(VocabularyWord word, VocabularyDtos.VocabularyWordUpsertRequest request) {
        word.setWord(request.word().trim().toLowerCase());
        word.setSimpleMeaning(request.simpleMeaning().trim());
        word.setDetailedMeaning(request.detailedMeaning());
        word.setPartOfSpeech(request.partOfSpeech().trim().toLowerCase());
        word.setLevel(request.level());
        word.setIeltsBandMin(request.ieltsBandMin());
        word.setIeltsBandMax(request.ieltsBandMax());
        word.setTags(VocabularyMapper.normalizedSet(request.tags()));
        word.setSynonyms(VocabularyMapper.normalizedSet(request.synonyms()));
        word.setAntonyms(VocabularyMapper.normalizedSet(request.antonyms()));
        word.setCollocations(VocabularyMapper.normalizedSet(request.collocations()));
        word.setCommonMistake(request.commonMistake());
        word.setPremium(request.premium());
    }

    private static String normalizeQuery(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toLowerCase();
    }

    private static void validateBandRange(Double minBand, Double maxBand) {
        if (maxBand < minBand) {
            throw new ValidationException("IELTS max band must be greater than or equal to min band");
        }
    }
}
