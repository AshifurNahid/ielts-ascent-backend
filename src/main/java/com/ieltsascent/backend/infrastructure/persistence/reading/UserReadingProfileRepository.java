package com.ieltsascent.backend.infrastructure.persistence.reading;

import com.ieltsascent.backend.domain.readingtest.UserReadingProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingProfileRepository extends JpaRepository<UserReadingProfile, Long> {
    Optional<UserReadingProfile> findByUserId(Long userId);
}
