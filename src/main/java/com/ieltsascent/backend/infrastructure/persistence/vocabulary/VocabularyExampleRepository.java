package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.VocabularyExample;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyExampleRepository extends JpaRepository<VocabularyExample, UUID> {
    List<VocabularyExample> findByVocabularyWordId(UUID vocabularyWordId);
}
