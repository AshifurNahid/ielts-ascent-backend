package com.ieltsascent.backend.infrastructure.persistence.grammar;

import com.ieltsascent.backend.domain.grammar.UserGrammarProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrammarProfileRepository extends JpaRepository<UserGrammarProfile, UUID> {
    Optional<UserGrammarProfile> findByUserId(UUID userId);
}
