package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.UserWritingProfile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWritingProfileRepository extends JpaRepository<UserWritingProfile, UUID> {
    Optional<UserWritingProfile> findByUserId(UUID userId);
}
