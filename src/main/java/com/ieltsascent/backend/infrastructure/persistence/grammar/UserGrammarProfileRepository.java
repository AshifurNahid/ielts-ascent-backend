package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrammarProfileRepository extends JpaRepository<UserGrammarProfile, Long> {
    Optional<UserGrammarProfile> findByUserId(Long userId);
}
