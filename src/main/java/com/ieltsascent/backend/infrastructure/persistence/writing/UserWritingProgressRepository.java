package com.ieltsascent.backend.infrastructure.persistence.writing;

import com.ieltsascent.backend.domain.writing.UserWritingProgress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWritingProgressRepository extends JpaRepository<UserWritingProgress, UUID> {
    Optional<UserWritingProgress> findByUserId(UUID userId);
}
