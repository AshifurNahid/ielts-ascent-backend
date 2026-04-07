package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.auth.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("""
        SELECT p
        FROM User u
        JOIN u.profile p
        WHERE u.id = :userId
        """)
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);
}
