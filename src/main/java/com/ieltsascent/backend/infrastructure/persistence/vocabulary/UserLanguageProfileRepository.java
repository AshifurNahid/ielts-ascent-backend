package com.ieltsascent.backend.infrastructure.persistence.vocabulary;

import com.ieltsascent.backend.domain.vocabulary.UserLanguageProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLanguageProfileRepository extends JpaRepository<UserLanguageProfile, Long> {
    Optional<UserLanguageProfile> findByUserId(Long userId);
}
