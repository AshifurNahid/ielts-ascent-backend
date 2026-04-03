package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLanguageProfileRepository extends JpaRepository<UserLanguageProfile, UUID> {
    Optional<UserLanguageProfile> findByUserId(UUID userId);
}
