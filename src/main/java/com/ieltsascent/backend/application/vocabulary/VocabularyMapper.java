package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.api.vocabulary.dto.VocabularyDtos;
import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import com.ieltsascent.backend.domain.vocabulary.VocabularyWord;
import java.util.Comparator;
import java.util.Set;

public final class VocabularyMapper {
    private VocabularyMapper() {
    }

    public static VocabularyDtos.VocabularyWordResponse toWordResponse(VocabularyWord word) {
        return new VocabularyDtos.VocabularyWordResponse(
            word.getId(),
            word.getWord(),
            word.getSimpleMeaning(),
            word.getDetailedMeaning(),
            word.getPartOfSpeech(),
            word.getLevel(),
            word.getIeltsBandMin(),
            word.getIeltsBandMax(),
            word.getTags(),
            word.getSynonyms(),
            word.getAntonyms(),
            word.getCollocations(),
            word.getCommonMistake(),
            word.getPremium(),
            word.getStatus(),
            word.getExamples().stream()
                .sorted(Comparator.comparing(VocabularyExample::getCreatedAt))
                .map(VocabularyMapper::toExampleResponse)
                .toList(),
            word.getCreatedAt(),
            word.getUpdatedAt()
        );
    }

    public static VocabularyDtos.VocabularyExampleResponse toExampleResponse(VocabularyExample example) {
        return new VocabularyDtos.VocabularyExampleResponse(
            example.getId(),
            example.getType(),
            example.getSentence(),
            example.getAiGenerated(),
            example.getApproved(),
            example.getCreatedAt(),
            example.getUpdatedAt()
        );
    }

    public static VocabularyDtos.UserLanguageProfileResponse toLanguageProfileResponse(UserLanguageProfile profile) {
        return new VocabularyDtos.UserLanguageProfileResponse(
            profile.getUserId(),
            profile.getEnglishLevel(),
            profile.getCurrentIeltsBand(),
            profile.getTargetIeltsBand(),
            profile.getWeakTags(),
            profile.getPremiumUser()
        );
    }

    public static Set<String> normalizedSet(Set<String> source) {
        if (source == null || source.isEmpty()) {
            return Set.of();
        }
        return source.stream()
            .map(String::trim)
            .filter(value -> !value.isBlank())
            .map(String::toLowerCase)
            .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
    }
}
