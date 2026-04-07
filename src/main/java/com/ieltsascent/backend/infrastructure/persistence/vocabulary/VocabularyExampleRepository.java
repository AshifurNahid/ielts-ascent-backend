package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyExampleRepository extends JpaRepository<VocabularyExample, Long> {
    List<VocabularyExample> findByVocabularyWordId(Long vocabularyWordId);
}
