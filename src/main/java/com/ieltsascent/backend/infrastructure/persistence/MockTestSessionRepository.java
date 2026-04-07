package com.ieltsascent.backend.infrastructure.persistence;

import com.ieltsascent.backend.domain.mocktest.MockTestSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockTestSessionRepository extends JpaRepository<MockTestSession, Long> {
}
