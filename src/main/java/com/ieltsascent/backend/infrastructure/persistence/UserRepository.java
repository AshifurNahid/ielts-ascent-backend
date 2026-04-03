package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.auth.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Override
    @EntityGraph(attributePaths = "profile")
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);
}
