package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.GrammarDrillTemplate;
import com.ieltsascent.backend.domain.grammar.GrammarQuestionType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarDrillTemplateRepository extends JpaRepository<GrammarDrillTemplate, UUID> {
    List<GrammarDrillTemplate> findByActiveTrueOrderByCreatedAtAsc();

    Optional<GrammarDrillTemplate> findByTypeAndActiveTrue(GrammarQuestionType type);
}
