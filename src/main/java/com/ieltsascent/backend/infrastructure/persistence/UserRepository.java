package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.auth.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    @EntityGraph(attributePaths = "profile")
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
