package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.UserGrammarProgress;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrammarProgressRepository extends JpaRepository<UserGrammarProgress, UUID> {
    List<UserGrammarProgress> findByUserId(UUID userId);

    Optional<UserGrammarProgress> findByUserIdAndTopicId(UUID userId, UUID topicId);
}
