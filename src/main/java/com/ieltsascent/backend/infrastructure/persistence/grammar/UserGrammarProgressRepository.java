package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.UserGrammarProgress;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrammarProgressRepository extends JpaRepository<UserGrammarProgress, Long> {
    List<UserGrammarProgress> findByUserId(Long userId);

    Optional<UserGrammarProgress> findByUserIdAndTopicId(Long userId, Long topicId);
}
