package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.UserReadingProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingProfileRepository extends JpaRepository<UserReadingProfile, UUID> {
    Optional<UserReadingProfile> findByUserId(UUID userId);
}
