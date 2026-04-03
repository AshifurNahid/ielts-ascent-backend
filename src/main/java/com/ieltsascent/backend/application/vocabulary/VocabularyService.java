package com.ieltsascent.backend.application.vocabulary;

import com.ieltsascent.backend.domain.content.VocabularyItem;
import com.ieltsascent.backend.infrastructure.persistence.VocabularyItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocabularyService {
    private final VocabularyItemRepository vocabularyItemRepository;

    public VocabularyOverview getVocabularyOverview(String query, String category, Pageable pageable) {
        String normalizedQuery = normalizeFilter(query);
        String normalizedCategory = normalizeFilter(category);

        Page<VocabularyItem> items = vocabularyItemRepository.searchVocabulary(
            normalizedQuery,
            normalizedCategory,
            pageable
        );
        List<VocabularyItemRepository.VocabularyCategoryCount> categories = vocabularyItemRepository.countByCategory();

        return new VocabularyOverview(items, categories);
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }

    public record VocabularyOverview(
        Page<VocabularyItem> items,
        List<VocabularyItemRepository.VocabularyCategoryCount> categories
    ) {
        public long totalCount() {
            return items.getTotalElements();
        }
    }
}
